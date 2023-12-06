package lol.schroeder.aoc23

import kotlin.system.measureNanoTime

abstract class Day {
    val red = "\u001b[31m"
    val green = "\u001b[32m"
    val yellow = "\u001b[33m"
    val gray = "\u001B[90m"
    val bold = "\u001b[1m"
    val reset = "\u001b[0m"

    abstract fun part1(): Any
    abstract fun part2(): Any

    fun solve() {
        println("$red::: $green$bold${this::class.simpleName}$reset $red:::$reset")
        measureNanoTime {
            println("${bold}Part 1:$reset $yellow${part1()}$reset")
        }.also { println("$gray=> ⏱️ $it ns$reset") }

        measureNanoTime {
            println("${bold}Part 2:$reset $yellow${part2()}$reset")
        }.also { println("$gray=> ⏱️ $it ns$reset") }

        println()
    }
}