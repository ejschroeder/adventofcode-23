package lol.schroeder.aoc23

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day11Test {

    @Test
    fun part1() {
        val input = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent().lines()

        val result = Day11(input).part1()

        expectThat(result) isEqualTo 374
    }

    @Test
    fun part2() {
        val input = """

        """.trimIndent().lines()

        val result = Day11(input).part2()

        expectThat(result) isEqualTo null
    }
}