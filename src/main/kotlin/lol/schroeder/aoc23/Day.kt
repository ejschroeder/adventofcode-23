package lol.schroeder.aoc23

import kotlin.system.measureNanoTime

abstract class Day {
    abstract fun part1(): Any
    abstract fun part2(): Any

    fun solve() {
        println("$BLUE$OSF$TSF$OSF$TSF$OSF 🎄$RED$BOLD${this::class.simpleName}$RESET 🎄$BLUE$OSF$TSF$OSF$TSF$OSF$RESET")
        measureNanoTime {
            println("🌟 ${BOLD}Part 1:$RESET $YELLOW${part1()}$RESET")
        }.also { println("$GRAY$ARROW ⏱️ $it ns$RESET") }

        measureNanoTime {
            println("🌟 ${BOLD}Part 2:$RESET $YELLOW${part2()}$RESET")
        }.also { println("$GRAY$ARROW ⏱️ $it ns$RESET") }

        println()
    }

    companion object {
        const val BLUE = "\u001b[94m"
        const val RED = "\u001b[31m"
        const val GREEN = "\u001b[32m"
        const val YELLOW = "\u001b[33m"
        const val GRAY = "\u001B[90m"
        const val BOLD = "\u001b[1m"
        const val RESET = "\u001b[0m"
        const val ARROW = "\u25B6"
        const val OSF = "\u204E"
        const val TSF = "\u2051"
        const val DOTS = "\u259E"
    }
}