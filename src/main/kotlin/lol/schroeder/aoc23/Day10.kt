package lol.schroeder.aoc23

class Day10(private val input: List<String> = readInputLines("day10")) : Day() {
    data class Coordinate(val row: Int, val col: Int)

    enum class Direction(private val rowOffset: Int, private val colOffset: Int) {
        NORTH(-1, 0),
        EAST(0, 1),
        SOUTH(1, 0),
        WEST(0, -1);

        fun from(coordinate: Coordinate) = Coordinate(coordinate.row + rowOffset, coordinate.col + colOffset)
        fun opposingDirection() = when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }
    }

    private fun Char.getNeighborDirections() = when (this) {
        '|' -> listOf(Direction.NORTH, Direction.SOUTH)
        '-' -> listOf(Direction.EAST, Direction.WEST)
        'L' -> listOf(Direction.NORTH, Direction.EAST)
        'J' -> listOf(Direction.NORTH, Direction.WEST)
        '7' -> listOf(Direction.SOUTH, Direction.WEST)
        'F' -> listOf(Direction.EAST, Direction.SOUTH)
        'S' -> listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
        else -> throw IllegalStateException("Looking for neighbors of a non-pipe tile '$this'")
    }

    // ┓┃┛━┏┗

    private fun Char.toBorder() = when (this) {
        '|' -> "┃"
        '-' -> "━"
        'L' -> "┗"
        'J' -> "┛"
        '7' -> "┓"
        'F' -> "┏"
        else -> "$this"
    }

    override fun part1(): Any {
        val width = input.first().indices
        val height = input.indices

        val graph = input.flatMapIndexed { row, line ->
            line.withIndex().filter { it.value != '.' }.map { (col, cell) ->
                val coord = Coordinate(row, col)
                val validNeighbors = cell.getNeighborDirections()
                    .map { it to it.from(coord) }
                    .filter { (_, neighbor) -> neighbor.col in width && neighbor.row in height }
                    .filter { (direction, neighbor) ->
                        val neighborPipe = input[neighbor.row][neighbor.col]
                        neighborPipe != '.' && direction.opposingDirection() in neighborPipe.getNeighborDirections() }
                    .map { it.second }
                coord to validNeighbors
            }
        }.toMap()

        val startRow = input.indexOfFirst { it.contains('S') }
        val startCol = input[startRow].indexOfFirst { it == 'S' }

        return findDistances(Coordinate(startRow, startCol), graph).values.max()
    }

    override fun part2(): Any {
        val width = input.first().indices
        val height = input.indices

        val graph = input.flatMapIndexed { row, line ->
            line.withIndex().filter { it.value != '.' }.map { (col, cell) ->
                val coord = Coordinate(row, col)
                val validNeighbors = cell.getNeighborDirections()
                    .map { it to it.from(coord) }
                    .filter { (_, neighbor) -> neighbor.col in width && neighbor.row in height }
                    .filter { (direction, neighbor) ->
                        val neighborPipe = input[neighbor.row][neighbor.col]
                        neighborPipe != '.' && direction.opposingDirection() in neighborPipe.getNeighborDirections() }
                    .map { it.second }
                coord to validNeighbors
            }
        }.toMap()

        val startRow = input.indexOfFirst { it.contains('S') }
        val startCol = input[startRow].indexOfFirst { it == 'S' }

        val loopNodes = findDistances(Coordinate(startRow, startCol), graph).keys

        val contained = input.flatMapIndexed { row, line ->
            line.indices
                .filter { Coordinate(row, it) !in loopNodes }
                .filter { col -> coordinateWithinLoop(Coordinate(row, col), line, loopNodes) }
                .map { Coordinate(row, it) }
        }.toSet()

//        printMap(loopNodes, contained)

        return input.withIndex().sumOf { (row, line) ->
            line.indices.count { col -> Coordinate(row, col)  in contained}
        }
    }

    private fun printMap(loopNodes: Set<Coordinate>, contained: Set<Coordinate>) {
        input.forEachIndexed { row, line ->
            print(row.toString().padStart(3) + ": ")
            line.forEachIndexed { col, c ->
                if (Coordinate(row, col) in loopNodes)
                    print(c.toBorder())
                else if (Coordinate(row, col) in contained)
                    print("\u001B[31m@\u001B[0m")
                else
                    print(".")
            }
            println()
        }
    }

    fun coordinateWithinLoop(coordinate: Coordinate, line: String, loopNodes: Set<Coordinate>): Boolean {
        var inHorizontalLine = false
        var horizontalStart: Char? = null
        var count = 0
        for (col in coordinate.col..line.lastIndex) {
            val nextCoord = Coordinate(coordinate.row, col)
            val isLoopNode = nextCoord in loopNodes
            if (isLoopNode && inHorizontalLine && (line[col] == '7' || line[col] == 'J' || (line[col] == 'S' && line[col + 1] != '-') )) {
                inHorizontalLine = false
                when {
                    horizontalStart == 'F' && line[col] == 'J' -> count++
                    horizontalStart == 'L' && line[col] == '7' -> count++
                }
            } else if (isLoopNode && !inHorizontalLine && (line[col] == 'F' || line[col] == 'L' || (line[col] == 'S' && line[col + 1] == '-' ) )) {
                inHorizontalLine = true
                horizontalStart = line[col]
            } else if (isLoopNode && !inHorizontalLine) {
                count++
            }
        }
        return count != 0 && count.isOdd()
    }

    fun findDistances(start: Coordinate, graph: Map<Coordinate, List<Coordinate>>): Map<Coordinate, Int> {
        val distances = mutableMapOf<Coordinate, Int>().withDefault { Integer.MAX_VALUE }
        val queue = ArrayDeque<Coordinate>()

        queue.add(start)
        distances[start] = 0
        while (queue.isNotEmpty()) {
            val currentCoord = queue.removeFirst()
            val distanceToNext = distances.getValue(currentCoord) + 1

            val neighbors = graph.getValue(currentCoord)
            neighbors.forEach {
                val neighborDistance = distances.getValue(it)
                if (neighborDistance > distanceToNext) {
                    distances[it] = distanceToNext
                    queue.add(it)
                }
            }
        }
        return distances
    }
}

fun main() = Day10().solve()