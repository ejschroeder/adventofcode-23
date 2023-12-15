package lol.schroeder.aoc23

typealias DishPlatform = List<List<Char>>

class Day14(input: List<String> = readInputLines("day14")) : Day() {
    private val platform = input.map { it.toList() }.rotateCounterclockwise()

    override fun part1(): Any {
        return platform.map { simulateRow(it) }
            .sumOf { row -> row.withIndex()
                .filter { it.value.isRound() }
                .sumOf { row.size - it.index } }
    }

    override fun part2(): Any {
        val (repeat, cycleSize, cycleStart) = findSimulationLoop(platform)

        val remaining = 1_000_000_000 - cycleStart
        val remainingSteps = (remaining + 1) % cycleSize

        val final = (1..remainingSteps)
            .fold(repeat) { plat, _ -> spinCycle(plat) }

        return final.sumOf { row -> row.withIndex()
            .filter { it.value.isRound() }
            .sumOf { row.size - it.index } }
    }

    private tailrec fun findSimulationLoop(platform: DishPlatform, step: Int = 1, cache: MutableMap<DishPlatform, Int> = hashMapOf()): Triple<DishPlatform, Int, Int> {
        if (platform in cache) {
            val cycleStart = cache.getValue(platform)
            return Triple(platform, step - cycleStart, cycleStart)
        }

        val newPlatform = spinCycle(platform)
        return findSimulationLoop(newPlatform, step + 1, cache.apply { put(platform, step) })
    }

    private fun spinCycle(platform: DishPlatform): DishPlatform {
        return platform.map { simulateRow(it) } // north
            .rotateClockwise()
            .map { simulateRow(it) } // west
            .rotateClockwise()
            .map { simulateRow(it) } // south
            .rotateClockwise()
            .map { simulateRow(it) } // east
            .rotateClockwise()
    }

    private fun simulateRow(row: List<Char>): List<Char> {
        return simulateRow(row, 0, 0, mutableListOf())
    }

    private tailrec fun simulateRow(row: List<Char>, openIndex: Int, boulderIndex: Int, resultRow: MutableList<Char>): List<Char> {
        if (boulderIndex > row.lastIndex)
            return resultRow.apply { addAll(row.subList(openIndex, row.size).map { if (it == 'O') '.' else it }) }

        val boulderTile = row[boulderIndex]
        val (newOpenIndex, newBoulderIndex) = when {
            boulderTile.isRound() && (row[openIndex].isOpen() || row[openIndex].isRound()) -> {
                resultRow.add('O')
                Pair(openIndex + 1, boulderIndex + 1)
            }
            boulderTile.isRound() || (boulderTile.isSquare() && openIndex != boulderIndex) -> {
                resultRow.add(if (row[openIndex] == '#') '#' else '.')
                Pair(openIndex + 1, boulderIndex)
            }
            else -> Pair(openIndex, boulderIndex + 1)
        }

        return simulateRow(row, newOpenIndex, newBoulderIndex, resultRow)
    }

    private fun <T> List<List<T>>.rotateClockwise() = this.transpose().map { it.reversed() }
    private fun <T> List<List<T>>.rotateCounterclockwise() = this.map { it.reversed() }.transpose()
    private fun Char.isOpen() = this == '.'
    private fun Char.isRound() = this == 'O'
    private fun Char.isSquare() = this == '#'
}

fun main() = Day14().solve()