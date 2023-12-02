package lol.schroeder.aoc23

class Day01(private val input: List<String>) : Day() {
    override fun part1(): Any {
        return input.map { line -> line.first(Char::isDigit).toString() + line.last(Char::isDigit) }
            .sumOf { it.toInt() }
    }

    override fun part2(): Any {
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
}

fun main() = Day01(readInputLines("day01")).solve()