package lol.schroeder.aoc23

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day18Test {

    @Test
    fun part1() {
        val input = """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
        """.trimIndent().lines()

        val result = Day18(input).part1()

        expectThat(result) isEqualTo 62
    }

    @Test
    fun part2() {
    }
}