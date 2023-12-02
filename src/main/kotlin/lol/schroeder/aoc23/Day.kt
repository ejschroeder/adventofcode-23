package lol.schroeder.aoc23

import kotlin.system.measureNanoTime

abstract class Day {
    abstract fun part1(): Any
    abstract fun part2(): Any

    fun solve() {
        println("::: ${this::class.simpleName} :::")
        measureNanoTime {
            println("Part 1: ${part1()}")
        }.also { println("=> $it ns") }

        measureNanoTime {
            println("Part 2: ${part2()}")
        }.also { println("=> $it ns") }
    }
}