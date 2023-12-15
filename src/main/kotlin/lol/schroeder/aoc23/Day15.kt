package lol.schroeder.aoc23

class Day15(private val input: List<String> = readInputLines("day15")) : Day() {
    override fun part1(): Any {
        return input.flatMap { it.split(",") }
            .sumOf { it.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }.toInt() }
    }

    override fun part2(): Any {
        val lensConfiguration = input.flatMap { it.split(",") }
            .map(::toOperation)
            .fold(LensConfiguration(), ::applyOperation)

        return lensConfiguration.focusingPower
    }

    private fun applyOperation(lensConfiguration: LensConfiguration, op: Operation) = when (op) {
        is Operation.Add -> lensConfiguration.add(op.lens)
        is Operation.Remove -> lensConfiguration.remove(op.label)
    }

    private fun toOperation(str: String): Operation {
        if (str.endsWith('-')) {
            return Operation.Remove(str.substringBeforeLast('-'))
        }

        val (label, focalLength) = str.split("=")
        return Operation.Add(Lens(label, focalLength.toInt()))
    }

    sealed class Operation {
        data class Add(val lens: Lens) : Operation()
        data class Remove(val label: String): Operation()
    }

    data class Lens(val label: String, val focalLength: Int)

    class LensConfiguration {
        private val boxes = List(256) { mutableListOf<Lens>() }

        val focusingPower: Int get() = boxes.withIndex().sumOf { (boxId, box) ->
            box.withIndex().sumOf { (slotId, lens) ->
                (1 + boxId) * (1 + slotId) * lens.focalLength
            }
        }

        fun add(lens: Lens): LensConfiguration {
            val idx = lens.label.hash()
            val foundIdx = boxes[idx].indexOfFirst { it.label == lens.label }
            if (foundIdx < 0) boxes[idx].add(lens) else boxes[idx][foundIdx] = lens
            return this
        }

        fun remove(label: String): LensConfiguration {
            val idx = label.hash()
            boxes[idx].removeAll { it.label == label }
            return this
        }

        private fun String.hash() = this.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
    }
}

fun main() = Day15().solve()