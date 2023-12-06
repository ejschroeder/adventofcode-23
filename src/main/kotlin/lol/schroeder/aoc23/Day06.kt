package lol.schroeder.aoc23

class Day06(private val input: List<String> = readInputLines("day06")) : Day() {
    data class BoatRecord(val time: Long, val distance: Long) {
        tailrec fun countWins(speed: Int = 1, wins: Int = 0): Int {
            if (speed >= time) return wins

            val remainingTime = time - speed
            val dist = remainingTime * speed

            return countWins(speed + 1, if (dist > distance) wins + 1 else wins)
        }
    }

    override fun part1(): Any {
        val (times, distances) = input.map { it.extractInts() }
        val records = times.zip(distances).map { BoatRecord(it.first.toLong(), it.second.toLong()) }

        val wins = records.map { it.countWins() }
        return wins.product()
    }

    override fun part2(): Any {
        val (time, distance) = input.map { it.extractInts() }.map { it.joinToString("") }
        val record = BoatRecord(time.toLong(), distance.toLong())

        return record.countWins()
    }
}

fun main() = Day06().solve()