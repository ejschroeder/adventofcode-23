package lol.schroeder.aoc23

class Day10(private val input: List<String> = readInputLines("day10")) : Day() {

    override fun part1(): Any {
        val tileMap = TileMap(input.flatMap { it.asIterable() }, input.first().length)

        val start = tileMap.find('S')
        val distances = tileMap.getDistancesFrom(start)

        return distances.values.max()
    }

    override fun part2(): Any {
        val tileMap = TileMap(input.flatMap { it.asIterable() }, input.first().length)

        val start = tileMap.find('S')

        return tileMap.countContainedTiles(start)
    }

    data class TileMap(val tiles: List<Char>, val width: Int) {
        private val rows = 0..<height
        private val cols = 0..<width
        private val height get() = tiles.size / width
        private val graph = buildGraph()

        init { require(width * height == tiles.size) { "Width does not match size: $width * $height != ${tiles.size}" } }

        fun getDistancesFrom(coordinate: Coordinate): Map<Coordinate, Int> {
            val queue = ArrayDeque<Coordinate>().apply { add(coordinate) }
            val distances = mutableMapOf<Coordinate, Int>()
                .withDefault { Integer.MAX_VALUE }.apply { put(coordinate, 0) }
            return calculateDistances(graph, queue, distances)
        }

        fun countContainedTiles(loopStart: Coordinate): Int {
            val loop = getDistancesFrom(loopStart).keys
            return tiles.indices
                .map { coordinateOf(it) }
                .filter { it !in loop }
                .count { isCoordinateInsideLoop(it, loop) }
        }

        private fun isCoordinateInsideLoop(coordinate: Coordinate, loop: Set<Coordinate>): Boolean {
            return (coordinate.col..cols.last).count { col ->
                val coord = Coordinate(coordinate.row, col)
                val tile = this[coord]
                // ignores 'S', and will miscount if 'S' is 'L' or 'J'
                coord in loop && (tile == 'L' || tile == 'J' || tile == '|')
            }.isOdd()
        }

        private tailrec fun calculateDistances(graph: Map<Coordinate, List<Coordinate>>, queue: ArrayDeque<Coordinate>, distances: MutableMap<Coordinate, Int>): Map<Coordinate, Int> {
            if (queue.isEmpty()) return distances

            val coordinate = queue.removeFirst()
            val distanceToNext = distances.getValue(coordinate) + 1

            val neighbors = graph.getValue(coordinate)
            neighbors.filter { distances.getValue(it) > distanceToNext }.forEach {
                queue.add(it)
                distances[it] = distanceToNext
            }

            return calculateDistances(graph, queue, distances)
        }

        private fun buildGraph() = tiles.withIndex()
            .filter { it.value != '.' }
            .map { coordinateOf(it.index) }
            .associateWith { getValidNeighborsForTile(it) }

        private fun getValidNeighborsForTile(coordinate: Coordinate) = get(coordinate)
            .getNeighborDirections()
            .map { it to it.from(coordinate) }
            .filter { (_, neighbor) -> neighbor in this }
            .filter { (direction, neighborCoordinate) ->
                val neighbor = this[neighborCoordinate]
                neighbor != '.' && direction.opposingDirection() in neighbor.getNeighborDirections()
            }
            .map { it.second }

        fun find(char: Char) = coordinateOf(tiles.indexOf(char))
        fun coordinateOf(index: Int) = Coordinate(index / width, index % width)
        fun indexOf(coordinate: Coordinate) = coordinate.row * width + coordinate.col
        operator fun get(coordinate: Coordinate) = tiles[indexOf(coordinate)]
        operator fun contains(coordinate: Coordinate) = coordinate.row in rows && coordinate.col in cols

        override fun toString(): String {
            return rows.joinToString("\n") { row -> cols.joinToString("") { col -> get(Coordinate(row, col)).toBorder() } }
        }

        private fun Char.getNeighborDirections() = when (this) {
            '|' -> listOf(Direction.NORTH, Direction.SOUTH)
            '-' -> listOf(Direction.EAST, Direction.WEST)
            'L' -> listOf(Direction.NORTH, Direction.EAST)
            'J' -> listOf(Direction.NORTH, Direction.WEST)
            '7' -> listOf(Direction.SOUTH, Direction.WEST)
            'F' -> listOf(Direction.EAST, Direction.SOUTH)
            'S' -> listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
            else -> listOf()
        }

        private fun Char.toBorder() = when (this) {
            '|' -> "┃"
            '-' -> "━"
            'L' -> "┗"
            'J' -> "┛"
            '7' -> "┓"
            'F' -> "┏"
            else -> "$this"
        }
    }

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
}

fun main() = Day10().solve()