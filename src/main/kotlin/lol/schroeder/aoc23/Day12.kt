package lol.schroeder.aoc23

class Day12(private val input: List<String> = readInputLines("day12")) : Day() {
    override fun part1() = parseInput()
        .sumOf { countArrangements(it.first, it.second) }

    override fun part2() = parseInput()
        .map { (springs, groups) -> List(5) { springs }.joinToString("?") to List(5) { groups }.flatten() }
        .sumOf { (springs, groups) -> countArrangements(springs, groups) }

    data class ArrangementState(val springs: String, val groups: List<Int>)

    private fun countArrangements(springConditionReport: String, groupSizes: List<Int>): Long {
        return countValidArrangements("$springConditionReport.", groupSizes, mutableMapOf())
    }

    private fun countValidArrangements(springs: String, groups: List<Int>, cache: MutableMap<ArrangementState, Long>): Long {
        val key = ArrangementState(springs, groups)
        if (key in cache) {
            return cache.getValue(key)
        }

        if (groups.isEmpty()) {
            return if (springs.any { it.isDamaged() }) 0 else 1
        }

        val groupSize = groups.first()
        val remainingGroups = groups.drop(1)
        val windowSize = groupSize + 1

        val endOfLastWindow = springs.indexOf('#')
            .let { if (it == -1) springs.length else it + windowSize }
            .coerceAtMost(springs.length)

        return springs.substring(0, endOfLastWindow)
            .windowed(windowSize)
            .withIndex()
            .filter { (_, window) -> canWindowContainGroup(window, groupSize) }
            .sumOf { countValidArrangements(springs.substring(it.index + groupSize + 1), remainingGroups, cache) }
            .also { cache[key] = it }
    }

    private fun canWindowContainGroup(window: String, groupSize: Int): Boolean {
        val prefix = window.take(groupSize)
        val last = window.last()
        return (last.isUnknown() || last.isOperational()) && prefix.none { it.isOperational() }
    }
    private fun Char.isOperational() = this == '.'
    private fun Char.isDamaged() = this == '#'
    private fun Char.isUnknown() = this == '?'

    private fun parseInput() = input.map { it.split(" ") }
        .map { it[0] to it[1].extractInts() }
}

fun main() = Day12().solve()