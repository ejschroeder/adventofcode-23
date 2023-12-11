package lol.schroeder.aoc23

import kotlin.math.max
import kotlin.math.min

class Day11(private val input: List<String> = readInputLines("day11")) : Day() {
    private val galaxies = parseGalaxies()
    private val occupiedCols = galaxies.map { it.col }.fold(setOf(), Set<Int>::plus)
    private val occupiedRows = galaxies.map { it.row }.fold(setOf(), Set<Int>::plus)

    override fun part1() = galaxies.elementPairs()
        .sumOf { (first, second) -> getDistanceBetween(first, second) }

    override fun part2() = galaxies.elementPairs()
        .sumOf { (first, second) -> getDistanceBetween(first, second, expansionSize = 1_000_000) }

    private fun getDistanceBetween(first: Coordinate, second: Coordinate, expansionSize: Long = 2): Long {
        val minRow = min(first.row, second.row)
        val maxRow = max(first.row, second.row)
        val rowDistance = (minRow..<maxRow).sumOf { if (it in occupiedRows) 1 else expansionSize }

        val minCol = min(first.col, second.col)
        val maxCol = max(first.col, second.col)
        val colDistance = (minCol..<maxCol).sumOf { if (it in occupiedCols) 1 else expansionSize }

        return rowDistance + colDistance
    }

    private fun parseGalaxies() = input.flatMapIndexed { rowIdx, row ->
        row.withIndex()
            .filter { it.value == '#' }
            .map { Coordinate(rowIdx, it.index) }
    }

    data class Coordinate(val row: Int, val col: Int)
}

fun main() = Day11().solve()