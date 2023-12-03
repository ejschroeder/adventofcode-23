package lol.schroeder.aoc23

class Day03(private val input: List<String>) : Day() {

    override fun part1(): Any {
        val schematic = Schematic(input)

        val symbolIndexes = schematic.symbols.map { SchematicIndex(it.row, it.col) }.toHashSet()

        return schematic.numbers
            .filter { it.surroundingCells().any(symbolIndexes::contains) }
            .sumOf { it.value }
    }

    override fun part2(): Any {
        val schematic = Schematic(input)

        val gears = schematic.symbols.filter { it.value == '*' }

        val partNumbersByCoordinate = schematic.numbers
            .flatMap { num -> num.coordinates().map { it to num } }
            .toMap()

        return gears.map { it.surroundingCells().mapNotNull { index -> partNumbersByCoordinate[index] }.distinct() }
            .filter { it.size == 2 }
            .sumOf { it.first().value * it.last().value }
    }

    data class SchematicIndex(val row: Int, val col: Int)
    data class PartNumber(val value: Int, val row: Int, val cols: IntRange) {
        fun surroundingCells() = (row-1..row+1).flatMap { r ->
            ((cols.first - 1)..(cols.last+1)).map { c -> SchematicIndex(r, c) }
        }.filter { !(it.row == row && it.col in cols) }

        fun coordinates() = cols.map { SchematicIndex(row, it) }
    }
    data class Symbol(val value: Char, val row: Int, val col: Int) {
        fun surroundingCells() = (row-1..row+1).flatMap { r ->
            (col-1..col+1).map { c -> SchematicIndex(r, c) }
        }.filter { !(it.row == row && it.col == col) }
    }
    data class Schematic(val numbers: List<PartNumber>, val symbols: List<Symbol>)

    private fun Schematic(schematic: List<String>): Schematic {
        val partNumbers = schematic.flatMapIndexed { row, line -> extractNumbers(line, row) }
        val symbols = schematic.flatMapIndexed { row, line -> extractSymbols(line, row) }
        return Schematic(partNumbers, symbols)
    }

    private fun extractSymbols(line: String, row: Int) = line.withIndex()
        .filter { !it.value.isDigit() && it.value != '.' }
        .map { Symbol(it.value, row, it.index) }

    private tailrec fun extractNumbers(remainingLine: String, currentRow: Int, currentCol: Int = 0, partNumbers: List<PartNumber> = listOf()): List<PartNumber> {
        if (remainingLine.isEmpty()) {
            return partNumbers
        }

        if (!remainingLine.first().isDigit()) {
            val trimmedLine = remainingLine.dropWhile { !it.isDigit() }
            val skipped = remainingLine.length - trimmedLine.length
            return extractNumbers(trimmedLine, currentRow, currentCol + skipped, partNumbers)
        }

        val num = remainingLine.takeWhile { it.isDigit() }
        val nextCol = currentCol + num.length
        val newPartNumber = PartNumber(num.toInt(), currentRow, currentCol..<nextCol)
        return extractNumbers(remainingLine.substring(num.length), currentRow,  nextCol,partNumbers + newPartNumber)
    }
}

fun main() = Day03(readInputLines("day03")).solve()