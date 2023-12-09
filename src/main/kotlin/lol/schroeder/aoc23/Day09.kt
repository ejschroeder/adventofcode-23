package lol.schroeder.aoc23

class Day09(private val input: List<String> = readInputLines("day09")) : Day() {

    override fun part1() = parseInput().map(::nextSequenceValue).sum()
    override fun part2() = parseInput().map(::previousSequenceValue).sum()

    private fun nextSequenceValue(sequence: List<Int>) = calculateSequenceValue(sequence, calculatePrevious = false)
    private fun previousSequenceValue(sequence: List<Int>) = calculateSequenceValue(sequence, calculatePrevious = true)

    private fun calculateSequenceValue(sequence: List<Int>, calculatePrevious: Boolean): Int {
        if (sequence.all { it == 0 }) return 0

        val nextDifference = calculateSequenceValue(sequence.zipWithNext { a, b -> b - a }, calculatePrevious)

        return if (calculatePrevious) sequence.first() - nextDifference else sequence.last() + nextDifference
    }

    private fun parseInput() = input.map { line -> line.split(" ").map { it.toInt() } }
}

fun main() = Day09().solve()