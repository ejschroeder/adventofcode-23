package lol.schroeder.aoc23

class Day16(private val input: List<String> = readInputLines("day16")) : Day() {

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

        return countEnergizedTiles(grid, queue, seen)
    }

    private tailrec fun countEnergizedTiles(grid: List<List<Char>>, queue: ArrayDeque<Laser>, seen: MutableSet<Laser>): Int {
        if (queue.isEmpty()) return seen.distinctBy { it.row to it.col }.count()

        val laser = queue.removeFirst()
        seen.add(laser)

        val nextTiles = laser.next(grid[laser.row][laser.col])
            .filter { it !in seen && it.row in grid.indices && it.col in grid[it.row].indices }

        return countEnergizedTiles(grid, queue.apply { addAll(nextTiles) }, seen)
    }

    enum class Dir {
        UP, RIGHT, DOWN, LEFT
    }

    data class Laser(val row: Int, val col: Int, val dir: Dir) {

        fun next(tile: Char) = when (tile) {
            '\\' -> when(dir) {
                Dir.UP -> listOf(turnAndMove(Dir.LEFT))
                Dir.RIGHT -> listOf(turnAndMove(Dir.DOWN))
                Dir.DOWN -> listOf(turnAndMove(Dir.RIGHT))
                Dir.LEFT -> listOf(turnAndMove(Dir.UP))
            }
            '/' -> when(dir) {
                Dir.UP -> listOf(turnAndMove(Dir.RIGHT))
                Dir.RIGHT -> listOf(turnAndMove(Dir.UP))
                Dir.DOWN -> listOf(turnAndMove(Dir.LEFT))
                Dir.LEFT -> listOf(turnAndMove(Dir.DOWN))
            }
            '-' -> when (dir) {
                Dir.UP, Dir.DOWN -> listOf(turnAndMove(Dir.LEFT), turnAndMove(Dir.RIGHT))
                else -> listOf(straight())
            }
            '|' -> when (dir) {
                Dir.LEFT, Dir.RIGHT -> listOf(turnAndMove(Dir.UP), turnAndMove(Dir.DOWN))
                else -> listOf(straight())
            }
            else -> listOf(straight())
        }

        private fun turnAndMove(dir: Dir) = when (dir) {
            Dir.UP -> copy(row = row - 1, dir = dir)
            Dir.RIGHT -> copy(col = col + 1, dir = dir)
            Dir.DOWN -> copy(row = row + 1, dir = dir)
            Dir.LEFT -> copy(col = col - 1, dir = dir)
        }

        private fun straight() = when (dir) {
            Dir.UP -> copy(row = row - 1)
            Dir.RIGHT -> copy(col = col + 1)
            Dir.DOWN -> copy(row = row + 1)
            Dir.LEFT -> copy(col = col - 1)
        }
    }
}

fun main() = Day16().solve()