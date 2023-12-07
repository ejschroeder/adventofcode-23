package lol.schroeder.aoc23

class Day07(private val input: List<String> = readInputLines("day07")) : Day() {
    enum class HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE,
        FULL_HOUSE,
        FOUR,
        FIVE
    }

    data class Hand(val cards: String, val bid: Long, val allowJokers: Boolean = false) : Comparable<Hand> {
        private val handType: HandType

        init {
            val replacedHand = if (allowJokers) {
                val nonJokerFrequencies = cards.filter { it != 'J' }.groupingBy { it }.eachCount()
                val highestCount = nonJokerFrequencies.maxByOrNull { (_, v) -> v }?.key ?: 'A'
                cards.replace('J', highestCount)
            } else cards

            val cardCounts = replacedHand.groupingBy { it }.eachCount().values

            handType = when {
                5 in cardCounts -> HandType.FIVE
                4 in cardCounts -> HandType.FOUR
                3 in cardCounts && 2 in cardCounts -> HandType.FULL_HOUSE
                3 in cardCounts -> HandType.THREE
                cardCounts.count { it == 2 } == 2 -> HandType.TWO_PAIR
                2 in cardCounts -> HandType.ONE_PAIR
                cardCounts.size == 5 -> HandType.HIGH_CARD
                else -> throw RuntimeException("Couldn't parse hand $cards")
            }
        }

        private fun Char.faceValue() = when {
            this.isDigit() -> this.digitToInt()
            this == 'T' -> 10
            this == 'J' -> 11
            this == 'Q' -> 12
            this == 'K' -> 13
            this == 'A' -> 14
            this == 'W' -> 1
            else -> -1
        }

        override operator fun compareTo(other: Hand): Int {
            val thisHand = if (allowJokers) cards.replace('J', 'W') else cards
            val otherHand = if (allowJokers) other.cards.replace('J', 'W') else other.cards
            val result = this.handType compareTo other.handType

            return if (result != 0) result else
                thisHand.zip(otherHand) { a, b -> a.faceValue() compareTo b.faceValue() }
                    .first { it != 0 }
        }
    }

    override fun part1(): Any {
        val hands = input.map { it.split(" ") }.map { Hand(it.first(), it.last().toLong()) }
        return hands.sorted().withIndex().sumOf { (it.index + 1) * it.value.bid }
    }

    override fun part2(): Any {
        val hands = input.map { it.split(" ") }.map { Hand(it.first(), it.last().toLong(), allowJokers = true) }
        return hands.sorted().withIndex().sumOf { (it.index + 1) * it.value.bid }
    }
}

fun main() = Day07().solve()