package lol.schroeder.aoc23

import org.junit.jupiter.api.Test

import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day09Test {

    @Test
    fun part1() {
        val input = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent().lines()

        val result = Day09(input).part1()

        expectThat(result) isEqualTo 114
    }

    @Test
    fun part2() {
        val input = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent().lines()

        val result = Day09(input).part2()

        expectThat(result) isEqualTo 2
    }
}