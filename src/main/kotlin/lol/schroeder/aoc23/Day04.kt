package lol.schroeder.aoc23

import kotlin.math.pow

class Day04(private val input: List<String>) : Day() {
    override fun part1(): Any {
        return input.map { it.substringAfter(": ").split(" | ") }
            .mapIndexed { idx, groups -> ScratchCard(idx, groups.first().extractInts().toSet(), groups.last().extractInts()) }
            .sumOf { it.score() }
    }

    override fun part2(): Any {
        val cardCounts = input.map { it.substringAfter(": ").split(" | ") }
            .mapIndexed { idx, groups -> ScratchCard(idx, groups.first().extractInts().toSet(), groups.last().extractInts()) }
            .fold(mutableMapOf(), ::addWonScratchCards)

        return cardCounts.values.sum()
    }

    private fun addWonScratchCards(cardCounts: MutableMap<Int, Int>, scratchCard: ScratchCard): MutableMap<Int, Int> {
        val matchCount = scratchCard.matchCount()
        val cardCount = cardCounts.getOrDefault(scratchCard.id, 0) + 1
        cardCounts[scratchCard.id] = cardCount

        repeat(matchCount) {
            val key = scratchCard.id + it + 1
            cardCounts[key] = cardCounts.getOrDefault(key, 0) + cardCount
        }

        return cardCounts
    }

    data class ScratchCard(val id: Int, val winningNumbers: Set<Int>, val numbers: List<Int>) {
        fun matchCount() = numbers.count { it in winningNumbers }
        fun score(): Int {
            val count = matchCount()
            return if (count == 0) 0 else 2.toDouble().pow(count - 1).toInt()
        }
    }
}

fun main() = Day04(readInputLines("day04")).solve()