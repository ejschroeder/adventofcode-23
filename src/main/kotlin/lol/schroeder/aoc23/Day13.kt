package lol.schroeder.aoc23

import kotlin.math.min

class Day13(private val input: List<String> = readInputLines("day13")) : Day() {
    data class Coordinate(val row: Int, val col: Int)
    data class Terrain(val terrain: List<List<Char>>) {
        fun reflectsAtRow(row: Int): List<Coordinate> {
            val nonMatches = mutableListOf<Coordinate>()
            val offset = min(terrain.lastIndex - row, row - 1)
            val reflectStart = row - 1
            for (currRowOffset in 0..offset) {
                val currRow = currRowOffset + row
                val reflectedRow = reflectStart - currRowOffset
                for (currCol in 0..terrain.last().lastIndex) {
                    if (terrain[currRow][currCol] != terrain[reflectedRow][currCol]) {
                        nonMatches.add(Coordinate(reflectedRow, currCol))
                    }
                }
            }
            return nonMatches
        }

        fun reflectsAtCol(col: Int): List<Coordinate> {
            val nonMatches = mutableListOf<Coordinate>()
            val offset = min(terrain.first().lastIndex - col, col - 1)
            val reflectStart = col - 1
            for (currColOffset in 0..offset) {
                val currCol = currColOffset + col
                val reflectedColumn = reflectStart - currColOffset
                for (currRow in 0..terrain.lastIndex) {
                    if (terrain[currRow][currCol] != terrain[currRow][reflectedColumn]) {
                        nonMatches.add(Coordinate(currRow, reflectedColumn))
                    }
                }
            }
            return nonMatches
        }
    }

    override fun part1(): Any {
        val terrain = input.splitOn { it.isBlank() }
            .map { Terrain(it.map(String::toList)) }

        val columnCount = terrain.withIndex().sumOf { (idx, terrain) ->
            countReflectedColumns(terrain).also { if (it > 0) println("Terrain ${idx + 1} reflects at column $it") }
        }
        val rowCount = terrain.withIndex().sumOf { (idx, terrain) ->
            countReflectedRows(terrain).also { if (it > 0) println("Terrain ${idx + 1} reflects at row $it") }
        }

        return columnCount + 100 * rowCount
    }

    override fun part2(): Any {
        val terrain = input.splitOn { it.isBlank() }
            .map { Terrain(it.map(String::toList)) }

        val columnCount = terrain.withIndex().sumOf { (idx, terrain) ->
            countReflectedColsWithSmudge(terrain).also { if (it > 0) println("Terrain ${idx + 1} reflects at column $it") }
        }
        val rowCount = terrain.withIndex().sumOf { (idx, terrain) ->
            countReflectedRowsWithSmudge(terrain).also { if (it > 0) println("Terrain ${idx + 1} reflects at row $it") }
        }

        return columnCount + 100 * rowCount
    }

    private fun countReflectedRowsWithSmudge(terrain: Terrain): Int {
        for (row in 1..terrain.terrain.lastIndex) {
            if (terrain.reflectsAtRow(row).size == 1) return row
        }
        return 0
    }

    private fun countReflectedColsWithSmudge(terrain: Terrain): Int {
        for (col in 1..terrain.terrain.first().lastIndex) {
            if (terrain.reflectsAtCol(col).size == 1) return col
        }
        return 0
    }

    private fun countReflectedRows(terrain: Terrain): Int {
        for (row in 1..terrain.terrain.lastIndex) {
            if (terrain.reflectsAtRow(row).isEmpty()) return row
        }
        return 0
    }

    private fun countReflectedColumns(terrain: Terrain): Int {
        for (col in 1..terrain.terrain.first().lastIndex) {
            if (terrain.reflectsAtCol(col).isEmpty()) return col
        }
        return 0
    }
}

fun main() = Day13().solve()