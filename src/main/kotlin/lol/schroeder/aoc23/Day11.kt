package lol.schroeder.aoc23

import kotlin.math.max
import kotlin.math.min

class Day11(private val input: List<String> = readInputLines("day11")) : Day() {
    data class Coordinate(val row: Int, val col: Int)
    override fun part1(): Any {
        val galaxies = input.flatMapIndexed { rowIdx, row -> row.withIndex()
                .filter { it.value == '#' }
                .map { Coordinate(rowIdx, it.index) }
        }

        val occupiedCols = galaxies.fold(mutableSetOf<Int>()) { columns, galaxy -> columns.apply { add(galaxy.col) } }
        val occupiedRows = galaxies.fold(mutableSetOf<Int>()) { columns, galaxy -> columns.apply { add(galaxy.row) } }

        return galaxies.elementPairs()
            .map { (first, second) -> getDistanceBetween(first, second, occupiedCols, occupiedRows) }
            .sum()
    }

    private fun getDistanceBetween(
        first: Coordinate,
        second: Coordinate,
        occupiedCols: Set<Int>,
        occupiedRows: Set<Int>,
        expansionSize: Long = 2
    ): Long {
        val minRow = min(first.row, second.row)
        val maxRow = max(first.row, second.row)

        val rowDistance = (minRow..<maxRow).sumOf { if (it in occupiedRows) 1 else expansionSize }

        val minCol = min(first.col, second.col)
        val maxCol = max(first.col, second.col)

        val colDistance = (minCol..<maxCol).sumOf { if (it in occupiedCols) 1 else expansionSize }

        return rowDistance + colDistance
    }

    override fun part2(): Any {
        val galaxies = input.flatMapIndexed { rowIdx, row -> row.withIndex()
            .filter { it.value == '#' }
            .map { Coordinate(rowIdx, it.index) }
        }

        val occupiedCols = galaxies.fold(mutableSetOf<Int>()) { columns, galaxy -> columns.apply { add(galaxy.col) } }
        val occupiedRows = galaxies.fold(mutableSetOf<Int>()) { columns, galaxy -> columns.apply { add(galaxy.row) } }

        return galaxies.elementPairs()
            .map { (first, second) -> getDistanceBetween(first, second, occupiedCols, occupiedRows, 1_000_000) }
            .sum()
    }
}

fun main() = Day11().solve()