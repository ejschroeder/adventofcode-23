package lol.schroeder.aoc23

import kotlin.math.max

class Day18(private val input: List<String> = readInputLines("day18")) : Day() {
    data class Coordinate(val row: Int, val col: Int) {
        operator fun plus(other: Coordinate) = copy(row = row + other.row, col = col + other.col)
    }

    enum class Direction(val offset: Coordinate) {
        UP(Coordinate(-1, 0)),
        RIGHT(Coordinate(0, 1)),
        DOWN(Coordinate(1, 0)),
        LEFT(Coordinate(0, -1))
    }

    private fun Direction(dir: String) = when(dir) {
        "U" -> Direction.UP
        "R" -> Direction.RIGHT
        "D" -> Direction.DOWN
        "L" -> Direction.LEFT
        else -> throw IllegalArgumentException("invalid direction string: $dir")
    }

    data class Instruction(val direction: Direction, val length: Int, val color: String) {
        fun coordinatesFrom(startingCoordinate: Coordinate) = (1..length).map {
            startingCoordinate + Coordinate(direction.offset.row * it, direction.offset.col * it)
        }
    }

    override fun part1(): Any {
        val instructions = input.map { it.split(" ") }
            .map { (dir, len, color) -> Instruction(Direction(dir), len.toInt(), color.substring(2..7)) }

        val boundary = instructions.fold(listOf(Coordinate(0, 0))) { coords, instr ->
            coords + instr.coordinatesFrom(coords.last())
        }.toSet()

        return calculateArea(boundary)
    }

    override fun part2(): Any {
        TODO("Not yet implemented")
    }

    private fun calculateArea(boundary: Set<Coordinate>): Int {
        val (minRow, maxRow) = boundary.minMaxOf { it.row }
        val (minCol, maxCol) = boundary.minMaxOf { it.col }
        val rowBounds = (minRow-1)..(maxRow+1)
        val colBounds = (minCol-1)..(maxCol+1)

        val startingCoordinate = Coordinate(rowBounds.first, colBounds.first)
        val seen = hashSetOf(startingCoordinate)
        val queue = ArrayDeque<Coordinate>().apply { add(startingCoordinate) }

        while (queue.isNotEmpty()) {
            val coord = queue.removeFirst()

            val neighbors = Direction.entries.map { coord + it.offset }
                .filter { it.row in rowBounds && it.col in colBounds && it !in seen && it !in boundary }

            queue.addAll(neighbors)
            seen.addAll(neighbors)
        }

        return (rowBounds.count() * colBounds.count()) - seen.count()
    }
}

fun main() = Day18().solve()