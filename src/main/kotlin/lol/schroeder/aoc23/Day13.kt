package lol.schroeder.aoc23

import kotlin.math.min

class Day13(private val input: List<String> = readInputLines("day13")) : Day() {
    data class Coordinate(val row: Int, val col: Int)
    data class TerrainMap(val terrain: List<List<Char>>) {
        private val columnMajorTerrain: List<List<Char>> by lazy { terrain.transpose() }

        fun findReflection(withSmudge: Boolean = false): Int {
            (1..terrain.lastIndex)
                .firstOrNull { reflectsAtRow(terrain, it).size == if (withSmudge) 1 else 0 }
                ?.let { return 100 * it }

            return (1..columnMajorTerrain.lastIndex)
                .firstOrNull { reflectsAtRow(columnMajorTerrain, it).size == if (withSmudge) 1 else 0 } ?: 0
        }

        private fun reflectsAtRow(terrain: List<List<Char>>, row: Int): List<Coordinate> {
            val reflectStart = row - 1
            val lastColumn = terrain.first().lastIndex
            val maxOffset = min(terrain.lastIndex - row, row - 1)

            return (0..maxOffset).flatMap { offset ->
                val currentRow = offset + row
                val reflectedRow = reflectStart - offset
                (0..lastColumn)
                    .filter { currentCol -> terrain[currentRow][currentCol] != terrain[reflectedRow][currentCol] }
                    .map { Coordinate(reflectedRow, it) }
            }
        }
    }

    override fun part1(): Any {
        val terrainMaps = input.splitOn { it.isBlank() }
            .map { TerrainMap(it.map(String::toList)) }

        return terrainMaps.sumOf(TerrainMap::findReflection)
    }

    override fun part2(): Any {
        val terrainMaps = input.splitOn { it.isBlank() }
            .map { TerrainMap(it.map(String::toList)) }

        return terrainMaps.sumOf { it.findReflection(withSmudge = true) }
    }
}

fun main() = Day13().solve()