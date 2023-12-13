package lol.schroeder.aoc23

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day13Test {

    @Test
    fun part1() {
        val input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent().lines()

        val result = Day13(input).part1()

        expectThat(result) isEqualTo 405
    }

    @Test
    fun part2() {
    }
}