package lol.schroeder.aoc23

class Day08(private val input: List<String> = readInputLines("day08")) : Day() {
    data class Node(val id: String, val leftDestination: String, val rightDestination: String)

    override fun part1(): Any {
        val instructions = input.first()
        val nodes = input.drop(2)
            .map { it.mapGroups("([A-Z]+) = \\(([A-Z]+), ([A-Z]+)\\)") { (source, left, right) -> Node(source, left, right) } }
            .associateBy { it.id }

        val steps = generateSequence { instructions.asIterable() }.flatten().runningFold("AAA") { acc, instr ->
            if (instr == 'L') nodes.getValue(acc).leftDestination else nodes.getValue(acc).rightDestination
        }.takeWhile { it != "ZZZ" }.toList()

        return steps.count()
    }

    data class State(val idx: Int, val node: String)

    data class StepState(val location: Long, val stepSize: Long)

    override fun part2(): Any {
        val instructions = input.first()
        val nodes = input.drop(2)
            .map { it.mapGroups("([A-Z1-9]+) = \\(([A-Z1-9]+), ([A-Z1-9]+)\\)") { (source, left, right) -> Node(source, left, right) } }
            .associateBy { it.id }

        val startingNodes = nodes.keys.filter { it.endsWith('A') }

        val cycles = startingNodes.map { findCycle(it, instructions, nodes) }

        val finalState = cycles.fold(StepState(0, 1)) { state, cycle ->
            val zOffset = cycle.zOffset.last()
            val firstOccurrence = generateSequence(state.location + state.stepSize) { it + state.stepSize }
                .first { (it + zOffset + cycle.offset).rem(cycle.period) == 0L }
            StepState(firstOccurrence, lcm(state.stepSize, cycle.period))
        }

        return finalState.location
    }

    fun lcm(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    data class Cycle(val period: Long, val offset: Int, val zOffset: List<Int>)

    fun findCycle(start: String, instructions: String, nodes: Map<String, Node>): Cycle {
        val seen = mutableSetOf<State>()
        var iter = 0
        var currentState = State(0, start)
        while (currentState !in seen) {
            val idx = iter % instructions.length
            seen.add(currentState)
            val instruction = instructions[idx]

            val newNode = if (instruction == 'L')
                nodes.getValue(currentState.node).leftDestination
            else
                nodes.getValue(currentState.node).rightDestination

            iter++
            val newIdx = if (idx == instructions.lastIndex) 0 else idx + 1
            currentState = State(newIdx, newNode)
        }

        val seenStates = seen.toList()
        val startOffset = seenStates.indexOf(currentState)

        val cycle = seenStates.drop(startOffset)
        val offsets = cycle.withIndex()
            .filter { it.value.node.endsWith('Z') }
            .map { it.index }

        return Cycle(cycle.size.toLong(), startOffset, offsets)
    }
}

fun main() = Day08().solve()