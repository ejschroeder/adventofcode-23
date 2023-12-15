package lol.schroeder.aoc23

class Day15(private val input: List<String> = readInputLines("day15")) : Day() {
    sealed class Operation {
        data class Add(val lens: Lens) : Operation()
        data class Remove(val label: String): Operation()
    }

    data class Lens(val label: String, val focalLength: Int) {
        val hash: Int get() = label.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
    }
    override fun part1(): Any {
        return input.flatMap { it.split(",") }.sumOf { it.hash() }
    }

    override fun part2(): Any {
        val map = List(256) { mutableListOf<Lens>() }

        val sorted = input.flatMap { it.split(",") }
            .map(::toOperation)
            .fold(map, ::applyOperation)

        val focusingPower = sorted.withIndex().sumOf { (boxId, box) ->
            box.withIndex().sumOf { (slotId, lens) ->
                (1 + boxId) * (1 + slotId) * lens.focalLength
            }
        }

        return focusingPower
    }

    private fun applyOperation(map: List<MutableList<Lens>>, op: Operation) = when (op) {
        is Operation.Add -> add(map, op.lens)
        is Operation.Remove -> remove(map, op.label)
    }

    private fun add(map: List<MutableList<Lens>>, lens: Lens): List<MutableList<Lens>> {
        val idx = lens.label.hash()
        val foundIdx = map[idx].indexOfFirst { it.label == lens.label }
        if (foundIdx < 0) map[idx].add(lens) else map[idx][foundIdx] = lens
        return map
    }

    private fun remove(map: List<MutableList<Lens>>, label: String): List<MutableList<Lens>> {
        val idx = label.hash()
        map[idx].removeAll { it.label == label }
        return map
    }

    private fun toOperation(str: String): Operation {
        if (str.endsWith('-')) {
            return Operation.Remove(str.substringBeforeLast('-'))
        }

        val (label, focalLength) = str.split("=")
        return Operation.Add(Lens(label, focalLength.toInt()))
    }

    private fun String.hash() = this.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
}

fun main() = Day15().solve()