package lol.schroeder.aoc23

class Day16(private val input: List<String> = readInputLines("day16")) : Day() {
    enum class Dir {
        UP, RIGHT, DOWN, LEFT
    }

    data class Laser(val row: Int, val col: Int, val dir: Dir) {

        fun next(tile: Char) = when (tile) {
            '\\' -> when(dir) {
                Dir.UP -> listOf(copy(dir = Dir.LEFT).straight())
                Dir.RIGHT -> listOf(copy(dir = Dir.DOWN).straight())
                Dir.DOWN -> listOf(copy(dir = Dir.RIGHT).straight())
                Dir.LEFT -> listOf(copy(dir = Dir.UP).straight())
            }
            '/' -> when(dir) {
                Dir.UP -> listOf(copy(dir = Dir.RIGHT).straight())
                Dir.RIGHT -> listOf(copy(dir = Dir.UP).straight())
                Dir.DOWN -> listOf(copy(dir = Dir.LEFT).straight())
                Dir.LEFT -> listOf(copy(dir = Dir.DOWN).straight())
            }
            '-' -> when (dir) {
                Dir.UP, Dir.DOWN -> listOf(copy(dir = Dir.LEFT).straight(), copy(dir = Dir.RIGHT).straight())
                else -> listOf(straight())
            }
            '|' -> when (dir) {
                Dir.LEFT, Dir.RIGHT -> listOf(copy(dir = Dir.UP).straight(), copy(dir = Dir.DOWN).straight())
                else -> listOf(straight())
            }
            else -> listOf(straight())
        }
        private fun straight() = when (dir) {
            Dir.UP -> copy(row = row - 1)
            Dir.RIGHT -> copy(col = col + 1)
            Dir.DOWN -> copy(row = row + 1)
            Dir.LEFT -> copy(col = col - 1)
        }
    }

    override fun part1(): Any {
        val grid = input.map { it.toList() }

        return countEnergizedTiles(grid, Laser(0, 0, Dir.RIGHT))
    }

    override fun part2(): Any {
        val grid = input.map { it.toList() }

        val startingPoints = listOf(
            grid.indices.map { Laser(it, 0, Dir.RIGHT) },
            grid.indices.map { Laser(it, grid[it].lastIndex, Dir.LEFT) },
            grid[0].indices.map { Laser(0, it, Dir.DOWN) },
            grid[0].indices.map { Laser(grid.lastIndex, it, Dir.UP) }
        ).flatten()

        return startingPoints.maxOf { countEnergizedTiles(grid, it) }
    }

    private fun countEnergizedTiles(grid: List<List<Char>>, start: Laser): Int {
        val queue = ArrayDeque<Laser>().apply { add(start) }
        val seen = hashSetOf<Laser>()
        while (queue.isNotEmpty()) {
            val laser = queue.removeFirst()
            seen.add(laser)
            val tile = grid[laser.row][laser.col]

            laser.next(tile)
                .filter { it !in seen && it.row in grid.indices && it.col in grid[it.row].indices }
                .forEach(queue::add)
        }

        return seen.distinctBy { it.row to it.col }.count()
    }
}

fun main() = Day16().solve()