package lol.schroeder.aoc23

import kotlin.math.max

class Day08(private val input: List<String> = readInputLines("day08")) : Day() {
    data class DesertMap(val instructions: String, val nodes: Map<String, Node>)
    data class Node(val id: String, val leftDestination: String, val rightDestination: String)
    data class Cycle(val period: Long, val offsets: List<Long>)
    data class State(val idx: Int, val node: String)
    data class StepState(val location: Long, val stepSize: Long)

    override fun part1(): Any {
        val (instructions, nodes) = parseInput()

        return instructions.asIterable()
            .cycle()
            .runningFold("AAA", nextNode(nodes))
            .takeWhile { it != "ZZZ" }
            .count()
    }

    override fun part2(): Any {
        val map = parseInput()

        return map.nodes.keys
            .filter { it.endsWith('A') }
            .map { findCycleRecursive(State(0, it), map) }
            .fold(StepState(0, 1), ::findNextExitNode)
            .location
    }

    private fun nextNode(nodes: Map<String, Node>) = { node: String, instr: Char ->
        if (instr == 'L') nodes.getValue(node).leftDestination
        else nodes.getValue(node).rightDestination
    }

    private fun findNextExitNode(state: StepState, cycle: Cycle): StepState {
        // Sample has multiple exit nodes in a cycle, but real data does not.
        // Taking the last works, but is probably wrong
        val offset = cycle.offsets.last()
        val nextMatchingExitNode = generateSequence(state.location + state.stepSize) { it + state.stepSize }
            .first { (it + offset).rem(cycle.period) == 0L }
        return StepState(nextMatchingExitNode, lcm(state.stepSize, cycle.period))
    }

    private tailrec fun findCycleRecursive(state: State, map: DesertMap, seen: MutableSet<State> = mutableSetOf()): Cycle {
        if (state in seen) {
            return seenToCycle(seen, state)
        }

        val instruction = map.instructions[state.idx]
        val newNode = nextNode(map.nodes)(state.node, instruction)
        val newIdx = if (state.idx == map.instructions.lastIndex) 0 else state.idx + 1
        seen.add(state)
        return findCycleRecursive(State(newIdx, newNode), map, seen)
    }

    private fun seenToCycle(seen: MutableSet<State>, state: State): Cycle {
        val startOffset = seen.indexOf(state)

        val cycle = seen.drop(startOffset)
        val offsets = cycle.withIndex()
            .filter { it.value.node.endsWith('Z') }
            .map { it.index + startOffset.toLong() }

        return Cycle(cycle.size.toLong(), offsets)
    }

    private fun parseInput(): DesertMap {
        val instructions = input.first()
        val nodes = input.drop(2)
            .map {
                it.mapGroups("([A-Z]+) = \\(([A-Z]+), ([A-Z]+)\\)") { (source, left, right) ->
                    Node(source, left, right)
                }
            }
            .associateBy { it.id }
        return DesertMap(instructions, nodes)
    }
}

fun main() = Day08().solve()