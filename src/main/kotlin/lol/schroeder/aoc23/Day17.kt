package lol.schroeder.aoc23

import java.util.PriorityQueue

class Day17(input: List<String> = readInputLines("day17")) : Day() {
    private val grid = input.map { it.map(Char::digitToInt) }

    enum class Dir { UP, RIGHT, DOWN, LEFT }
    data class CrucibleState(val row: Int, val col: Int, val dir: Dir, val steps: Int = 0) {
        fun nextStates(): List<CrucibleState> {
            return when (dir) {
                Dir.UP -> { listOf(
                    copy(row = row - 1, dir = Dir.UP, steps = steps + 1),
                    copy(col = col - 1, dir = Dir.LEFT, steps = 1),
                    copy(col = col + 1, dir = Dir.RIGHT, steps = 1))
                }
                Dir.RIGHT -> { listOf(
                    copy(row = row - 1, dir = Dir.UP, steps = 1),
                    copy(row = row + 1, dir = Dir.DOWN, steps = 1),
                    copy(col = col + 1, dir = Dir.RIGHT, steps = steps + 1))
                }
                Dir.DOWN -> { listOf(
                    copy(row = row + 1, dir = Dir.DOWN, steps = steps + 1),
                    copy(col = col - 1, dir = Dir.LEFT, steps = 1),
                    copy(col = col + 1, dir = Dir.RIGHT, steps = 1))
                }
                Dir.LEFT -> { listOf(
                    copy(row = row - 1, dir = Dir.UP, steps = 1),
                    copy(col = col - 1, dir = Dir.LEFT, steps = steps + 1),
                    copy(row = row + 1, dir = Dir.DOWN, steps = 1))
                }
            }
        }

    }

    override fun part1(): Any {
        val seen = hashSetOf<CrucibleState>()
        val queue = PriorityQueue<Pair<CrucibleState, Int>>(compareBy { it.second }).apply {
            add(CrucibleState(0,0, Dir.RIGHT) to 0)
        }

        while (queue.isNotEmpty()) {
            val (state, heatLoss) = queue.remove()

            if (state.row == grid.lastIndex && state.col == grid[state.row].lastIndex) {
                return heatLoss
            }

            state.nextStates()
                .filter { it.row in grid.indices && it.col in grid[it.row].indices && it.steps <= 3 && it !in seen }
                .map { it to (heatLoss + grid[it.row][it.col]) }
                .forEach {
                    seen.add(it.first)
                    queue.add(it)
                }
        }
        return -1
    }

    override fun part2(): Any {
        TODO("Not yet implemented")
    }
}

fun main() = Day17().solve()