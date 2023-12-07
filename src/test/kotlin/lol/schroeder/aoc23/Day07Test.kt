package lol.schroeder.aoc23

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day07Test {

    @Test
    fun part1() {
        val input = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent().lines()

        val result = Day07(input).part1()

        expectThat(result) isEqualTo 6440L
    }

    @Test
    fun part2() {
        val input = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent().lines()

        val result = Day07(input).part2()

        expectThat(result) isEqualTo 5905L
    }
}