package lol.schroeder.aoc23

import kotlin.math.abs
import kotlin.math.max

class Day18(private val input: List<String> = readInputLines("day18")) : Day() {

    override fun part1(): Any {
        val instructions = input.map { it.split(" ") }
            .map { (dir, len) -> Instruction(Direction(dir), len.toLong()) }

        return calculateInternalArea(instructions)
    }

    override fun part2(): Any {
        val instructions = input.map { it.split(" ")[2].substring(2..7) }
            .map { color -> Instruction(Direction(color.last().toString()), color.dropLast(1).toLong(16)) }

        return calculateInternalArea(instructions)
    }

    private fun calculateInternalArea(instructions: List<Instruction>): Long {
        val lines = instructions.fold(listOf(Coordinate(0, 0))) { acc, (direction, length) ->
            acc + (acc.last() + Coordinate(direction.offset.row * length, direction.offset.col * length))
        }.zipWithNext { a, b -> Line(a, b) }.filter { it.isVertical }

        val maxColumn = lines.maxOf { it.start.col }
        var currentColumn = lines.minOf { it.start.col }
        var currentRanges = listOf<LongRange>()
        var total = 0L
        while (currentColumn < maxColumn) {
            val ranges = lines.filter { it.start.col == currentColumn }.map(Line::rowBounds).toMutableList()

            currentRanges = currentRanges.map { activeRange ->
                val overlappingRanges = ranges.filter { it.overlaps(activeRange) && !activeRange.contains(it) }
                ranges.removeAll(overlappingRanges)
                overlappingRanges.fold(activeRange, LongRange::merge)
            }

            while (currentRanges.any { outer -> currentRanges.filter { outer != it }.any { outer.overlaps(it) } }) {
                currentRanges = currentRanges.map { current ->
                    currentRanges.filter { it.overlaps(current) }.fold(current, LongRange::merge)
                }
                    .distinct()
            }

            val nonOverlappingRanges = ranges.filter { range -> currentRanges.none { it.overlaps(range) } }
            currentRanges = currentRanges + nonOverlappingRanges
            ranges.removeAll(nonOverlappingRanges)

            val beforeRemoval = currentRanges

            val activeRanges = currentRanges.toMutableList()
            ranges.forEach { range ->
                val containingRange = activeRanges.find { it.contains(range) }!!
                activeRanges.remove(containingRange)
                if (containingRange == range) {
                    // do nothing
                } else if (containingRange.first == range.first) {
                    activeRanges.add((range.last)..containingRange.last)
                } else if (containingRange.last == range.last) {
                    activeRanges.add(containingRange.first..range.first)
                } else {
                    activeRanges.add(containingRange.first..range.first)
                    activeRanges.add(range.last..containingRange.last)
                }
            }

            currentRanges = activeRanges

            val nextColumn = lines.filter { it.start.col > currentColumn }.minOf { it.start.col }

            val diff = nextColumn - currentColumn
            total += beforeRemoval.sumOf { it.count() } + currentRanges.sumOf { it.count() * (diff - 1) }
            currentColumn = nextColumn
        }

        total += currentRanges.sumOf { it.count() }
        return total
    }

    data class Coordinate(val row: Long, val col: Long) {
        operator fun plus(other: Coordinate) = copy(row = row + other.row, col = col + other.col)
    }

    data class Line(val start: Coordinate, val end: Coordinate) {
        val isVertical get() = start.col == end.col
        val rowBounds get() = if (start.row < end.row) start.row..end.row else end.row..start.row
    }

    enum class Direction(val offset: Coordinate) {
        UP(Coordinate(-1, 0)),
        RIGHT(Coordinate(0, 1)),
        DOWN(Coordinate(1, 0)),
        LEFT(Coordinate(0, -1));
    }

    private fun Direction(dir: String) = when(dir) {
        "U", "3" -> Direction.UP
        "R", "0" -> Direction.RIGHT
        "D", "1" -> Direction.DOWN
        "L", "2" -> Direction.LEFT
        else -> throw IllegalArgumentException("invalid direction string: $dir")
    }

    data class Instruction(val direction: Direction, val length: Long)
}

fun main() = Day18().solve()