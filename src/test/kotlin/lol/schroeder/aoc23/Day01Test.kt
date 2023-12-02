package lol.schroeder.aoc23

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day01Test {

    @Test
    fun part1() {
        val input = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent().lines()

        val result = Day01(input).part1()

        expectThat(result) isEqualTo 142
    }

    @Test
    fun part2() {
        val input = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent().lines()

        val result = Day01(input).part2()

        expectThat(result) isEqualTo 281
    }
}