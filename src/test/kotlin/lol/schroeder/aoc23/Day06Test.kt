package lol.schroeder.aoc23

import org.junit.jupiter.api.Test

import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day06Test {

    @Test
    fun part1() {
        val input = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent().lines()

        val result = Day06(input).part1()

        expectThat(result) isEqualTo 288
    }

    @Test
    fun part2() {
        val input = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent().lines()

        val result = Day06(input).part2()

        expectThat(result) isEqualTo 71503
    }
}