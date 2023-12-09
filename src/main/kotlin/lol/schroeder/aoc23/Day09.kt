package lol.schroeder.aoc23

class Day09(private val input: List<String> = readInputLines("day09")) : Day() {
    override fun part1(): Any {
        val sequences = parseInput()

        return sequences.map(::getNextSequenceValue).sum()
    }

    override fun part2(): Any {
        val sequences = parseInput()

        return sequences.map{ it.reversed() }.map(::getNextHistoricalValue).sum()
    }

    private fun getNextSequenceValue(sequence: List<Int>) = getNextValue(mutableListOf(sequence))

    private tailrec fun getNextValue(sequences: MutableList<List<Int>>): Int {
        val sequence = sequences.last()
        if (sequence.all { it == 0 }) {
            println(sequences)

            return sequences.foldRight(0) { ints, acc -> acc + ints.last() }
        }

        sequences.add(sequence.zipWithNext { a, b -> b - a })

        return getNextValue(sequences)
    }

    private fun getNextHistoricalValue(sequence: List<Int>) = getNextOldValue(mutableListOf(sequence))

    private tailrec fun getNextOldValue(sequences: MutableList<List<Int>>): Int {
        val sequence = sequences.last()
        if (sequence.all { it == 0 }) {
            println(sequences)

            return sequences.foldRight(0) { ints, acc -> ints.last() - acc }
        }

        sequences.add(sequence.zipWithNext { a, b -> a - b })

        return getNextOldValue(sequences)
    }

    private fun parseInput() = input.map { line -> line.split(" ").map { it.toInt() } }
}

fun main() = Day09().solve()