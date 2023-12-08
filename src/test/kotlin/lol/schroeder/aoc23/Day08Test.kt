package lol.schroeder.aoc23

import org.junit.jupiter.api.Test

import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day08Test {

    @Test
    fun part1() {
        val input = """
            LLR
            
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().lines()

        val result = Day08(input).part1()

        expectThat(result) isEqualTo 6
    }

    @Test
    fun part2() {
        val input = """
            LR
            
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent().lines()

        val result = Day08(input).part2()

        expectThat(result) isEqualTo 6L
    }
}