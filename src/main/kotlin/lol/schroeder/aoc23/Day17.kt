package lol.schroeder.aoc23

import java.util.PriorityQueue

class Day17(input: List<String> = readInputLines("day17")) : Day() {
    private val grid = input.map { it.map(Char::digitToInt) }

    override fun part1() = findShortestPath(0, 3) ?: -1

    override fun part2() = findShortestPath(4, 10) ?: -1

    private fun findShortestPath(minStraight: Int, max: Int): Int? {
        val seen = hashSetOf<Crucible>()
        val queue = PriorityQueue<Pair<Crucible, Int>>(compareBy { it.second }).apply {
            add(Crucible(0,0, Dir.RIGHT) to 0)
        }

        while (queue.isNotEmpty()) {
            val (state, heatLoss) = queue.remove()

            if (state.row == grid.lastIndex && state.col == grid[state.row].lastIndex && state.steps >= minStraight) {
                return heatLoss
            }

            state.nextStates(minStraight, max)
                .filter { it.row in grid.indices && it.col in grid[it.row].indices && it !in seen }
                .map { it to (heatLoss + grid[it.row][it.col]) }
                .forEach {
                    seen.add(it.first)
                    queue.add(it)
                }
        }

        return null
    }

    enum class Dir { UP, RIGHT, DOWN, LEFT }
    data class Crucible(val row: Int, val col: Int, val dir: Dir, val steps: Int = 0) {
        fun nextStates(min: Int, max: Int) =
            if (steps in 1..<min) listOf(forward())
            else if (steps >= max) getTurns()
            else getTurns() + forward()

        private fun forward() = when (dir) {
            Dir.UP -> copy(row = row - 1, dir = Dir.UP, steps = steps + 1)
            Dir.RIGHT -> copy(col = col + 1, dir = Dir.RIGHT, steps = steps + 1)
            Dir.DOWN -> copy(row = row + 1, dir = Dir.DOWN, steps = steps + 1)
            Dir.LEFT -> copy(col = col - 1, dir = Dir.LEFT, steps = steps + 1)
        }

        private fun getTurns() = when (dir) {
            Dir.UP, Dir.DOWN -> listOf(
                copy(col = col - 1, dir = Dir.LEFT, steps = 1),
                copy(col = col + 1, dir = Dir.RIGHT, steps = 1))
            Dir.LEFT, Dir.RIGHT -> listOf(
                copy(row = row - 1, dir = Dir.UP, steps = 1),
                copy(row = row + 1, dir = Dir.DOWN, steps = 1))
        }
    }
}

fun main() = Day17().solve()