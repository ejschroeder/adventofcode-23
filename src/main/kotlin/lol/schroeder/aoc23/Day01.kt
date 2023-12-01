package lol.schroeder.aoc23

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line -> line.first(Char::isDigit).toString() + line.last(Char::isDigit) }
            .sumOf { it.toInt() }
    }

    fun part2(input: List<String>): Int {
        val nums = mapOf(
            "1" to "1",
            "2" to "2",
            "3" to "3",
            "4" to "4",
            "5" to "5",
            "6" to "6",
            "7" to "7",
            "8" to "8",
            "9" to "9",
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9"
        )

        return input.map {
            val first = it.findAnyOf(nums.keys)!!.second
            val last = it.findLastAnyOf(nums.keys)!!.second
            nums[first] + nums[last]
        }.sumOf { it.toInt() }
    }

    val input = readInputLines("day01")

    println("::: Day01 :::")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}