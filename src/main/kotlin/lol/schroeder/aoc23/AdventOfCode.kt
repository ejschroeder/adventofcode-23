package lol.schroeder.aoc23

val days = listOf(
    Day01(),
    Day02(),
    Day03(),
    Day04(),
    Day05(),
    Day06(),
    Day07(),
    Day08()
)

fun main() {
    days.forEach { it.solve() }
}