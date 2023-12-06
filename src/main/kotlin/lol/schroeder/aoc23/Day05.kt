package lol.schroeder.aoc23

import kotlin.math.min

typealias Almanac = Map<String, Day05.AlmanacMap>

class Day05(private val input: List<String> = readInputLines("day05")) : Day() {

    override fun part1(): Any {
        val seeds = input.first()
            .substringAfter("seeds: ")
            .split(" ")
            .map { Material("seed", it.toLong()) }
        val almanac = parseAlmanac(input)


        return seeds.minOf { getClosestLocationForMaterial(it, almanac) }
    }

    override fun part2(): Any {
        val seeds = input.first()
            .substringAfter("seeds: ")
            .split(" ")
            .chunked(2)
            .map { Material("seed", it.first().toLong(), it.last().toLong()) }
        val almanac = parseAlmanac(input)

        return seeds.minOf { getClosestLocationForMaterial(it, almanac) }
    }

    private fun parseAlmanac(input: List<String>) = input.drop(2)
        .splitOn { it.isBlank() }
        .map { AlmanacMap(it) }
        .associateBy { it.source }

    data class AlmanacMap(val source: String, val destination: String, val mappingRanges: List<MappingRange>) {
        tailrec fun splitSourceToDestinationRange(sourceRange: LongRange, destinationRanges: MutableList<LongRange> = mutableListOf()): List<LongRange> {
            if (sourceRange.isEmpty()) return destinationRanges

            val containingRange = mappingRanges.find { sourceRange.first in it }

            return if (containingRange == null) {
                val upperBound = mappingRanges
                    .filter { it.sourceStart > sourceRange.first && sourceRange.last >= it.sourceStart }
                    .minOfOrNull { it.sourceStart } ?: (sourceRange.last + 1)
                destinationRanges.add(sourceRange.first..<upperBound)
                splitSourceToDestinationRange(upperBound..sourceRange.last, destinationRanges)
            } else {
                val upperBound = min(sourceRange.last + 1, containingRange.sourceStart + containingRange.rangeLength)
                val offset = containingRange.destinationStart - containingRange.sourceStart
                destinationRanges.add((sourceRange.first + offset)..<(upperBound + offset))
                splitSourceToDestinationRange(upperBound..sourceRange.last, destinationRanges)
            }
        }
    }

    data class MappingRange(val sourceStart: Long, val destinationStart: Long, val rangeLength: Long) {
        operator fun contains(source: Long): Boolean {
            return source >= sourceStart && source < sourceStart + rangeLength
        }
    }

    data class Material(val type: String, val startId: Long, val rangeLength: Long = 1) {
        val range: LongRange get() = startId..<(startId + rangeLength)
    }

    private fun AlmanacMap(input: List<String>): AlmanacMap {
        val (source, destination) = input.first().substringBefore(" ").split("-to-")
        val ranges = input.drop(1).map { line ->
            val sourceDestNums = line.split(" ").map { it.toLong() }
            MappingRange(sourceDestNums[1], sourceDestNums[0], sourceDestNums[2])
        }
        return AlmanacMap(source, destination, ranges)
    }

    private fun getClosestLocationForMaterial(material: Material, almanac: Almanac): Long {
        val locationRanges = getLocationRangesForTypeRange(material.type, material.range, almanac)
        return locationRanges.minOf { it.first }
    }

    private fun getLocationRangesForTypeRange(material: String, range: LongRange, almanac: Almanac): List<LongRange> {
        val map = almanac[material] ?: return listOf(range)
        val splitRanges = map.splitSourceToDestinationRange(range)
        return splitRanges.flatMap { getLocationRangesForTypeRange(map.destination, it, almanac) }
    }
}

fun main() = Day05().solve()