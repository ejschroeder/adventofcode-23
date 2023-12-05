package lol.schroeder.aoc23

import kotlin.math.min

class Day05(private val input: List<String>) : Day() {
    data class Material(val type: String, val startId: Long, val rangeLength: Long = 1)
    data class MappingRange(val sourceStart: Long, val destinationStart: Long, val rangeLength: Long) {
        fun getDestination(source: Long): Long {
            val offset = source - sourceStart
            return destinationStart + offset
        }

        operator fun contains(source: Long): Boolean {
            return source >= sourceStart && source < sourceStart + rangeLength
        }
    }
    data class AlmanacMap(val source: String, val destination: String, val mappingRanges: List<MappingRange>) {
        fun getDestination(source: Long): Long {
            return mappingRanges.firstOrNull { source in it }?.getDestination(source) ?: source
        }

        fun splitToDestinationRanges(sourceRange: LongRange): List<LongRange> {
            val ranges = mutableListOf<LongRange>()
            var currentLowerBound = sourceRange.first

            while (currentLowerBound < sourceRange.last) {
                val containingRange = mappingRanges.find { currentLowerBound in it }
                currentLowerBound = if (containingRange == null) {
                    val nextLowerBound = mappingRanges
                        .filter { it.sourceStart > currentLowerBound }
                        .minOfOrNull { it.sourceStart } ?: (sourceRange.last + 1)
                    val newRange = currentLowerBound..<nextLowerBound
                    ranges.add(newRange)
                    nextLowerBound
                } else {
                    val nextLowerBound = min(sourceRange.last + 1, containingRange.sourceStart + containingRange.rangeLength)
                    val offset = containingRange.destinationStart - containingRange.sourceStart
                    ranges.add((currentLowerBound + offset)..<(nextLowerBound + offset))
                    nextLowerBound
                }
            }

            return ranges
        }
    }

    fun AlmanacMap(input: List<String>): AlmanacMap {
        val (source, destination) = input.first().substringBefore(" ").split("-to-")
        val ranges = input.drop(1).map { line ->
            val sourceDestNums = line.split(" ").map { it.toLong() }
            MappingRange(sourceDestNums[1], sourceDestNums[0], sourceDestNums[2])
        }
        return AlmanacMap(source, destination, ranges)
    }

    override fun part1(): Any {
        val seeds = input.first()
            .substringAfter("seeds: ")
            .split(" ")
            .map { Material("seed", it.toLong()) }
        val maps = input.drop(2)
            .splitOn { it.isBlank() }
            .map { AlmanacMap(it) }
            .associateBy { it.source }

        return seeds.map { getFinalValue(it, maps) }.minOf { it.startId }
    }

    private tailrec fun getFinalValue(material: Material, maps: Map<String, AlmanacMap>): Material {
        val map = maps[material.type] ?: return material

        val nextDestination = map.destination
        val newValue = map.getDestination(material.startId)
        val nextMaterial = Material(nextDestination, newValue)
        return getFinalValue(nextMaterial, maps)
    }

    private fun getFinalP2Value(material: String, range: LongRange, maps: Map<String, AlmanacMap>): List<LongRange> {
        val map = maps[material] ?: return listOf(range)
        val splitRanges = map.splitToDestinationRanges(range)
        return splitRanges.flatMap { getFinalP2Value(map.destination, it, maps) }
    }

    override fun part2(): Any {
        val seeds = input.first()
            .substringAfter("seeds: ")
            .split(" ")
            .chunked(2)
            .map { it.first().toLong()..<(it.first().toLong() + it.last().toLong()) }

        val maps = input.drop(2).splitOn { it.isBlank() }.map { AlmanacMap(it) }.associateBy { it.source }

        val ranges = seeds.flatMap { getFinalP2Value("seed", it, maps) }
        return ranges.minOf { it.first }
    }
}

fun main() = Day05(readInputLines("day05")).solve()