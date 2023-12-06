package lol.schroeder.aoc23

import java.util.regex.Pattern

class Day02(private val input: List<String> = readInputLines("day02")) : Day() {

    private fun isValidPull(pull: Pair<String, Int>): Boolean {
        return pull.first == "red" && pull.second <= 12
                || pull.first == "green" && pull.second <= 13
                || pull.first == "blue" && pull.second <= 14
    }

    private fun toPair(pull: String): Pair<String, Int> {
        val (num, color) = pull.split(" ")
        return color to num.toInt()
    }

    private fun parseGames() = input
        .asSequence()
        .map { it.substringAfter(": ") }
        .map { it.split(Pattern.compile("[,;] ")) }
        .map { it.map(::toPair) }

    override fun part1(): Any {
        return parseGames()
            .withIndex()
            .filter { it.value.all(::isValidPull) }
            .sumOf { it.index + 1 }
    }

    override fun part2(): Any {
        return parseGames()
            .map { game ->
                val redMax = game.filter { it.first == "red" }.maxOf { it.second }
                val greenMax = game.filter { it.first == "green" }.maxOf { it.second }
                val blueMax = game.filter { it.first == "blue" }.maxOf { it.second }
                redMax * greenMax * blueMax
            }.sum()
    }
}

fun main() = Day02().solve()