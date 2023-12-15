package lol.schroeder.aoc23

import org.junit.jupiter.api.Test

import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day15Test {

    @Test
    fun part1() {
        val input = """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
        """.trimIndent().lines()

        val result = Day15(input).part1()

        expectThat(result) isEqualTo 1320
    }

    @Test
    fun part2() {
        val input = """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
        """.trimIndent().lines()

        val result = Day15(input).part2()

        expectThat(result) isEqualTo 145
    }
}