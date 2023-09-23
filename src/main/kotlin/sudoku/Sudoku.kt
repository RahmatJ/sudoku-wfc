package sudoku

import board.Board

class Sudoku {
    private var board = Array(Board.BOARD_SIZE) { Array(Board.BOARD_SIZE) { 0 } }
    private var possibilityBoard = Array(Board.BOARD_SIZE) { Array(Board.BOARD_SIZE) { Cell() } }

    init {
        initiatePossibilityBoard()
    }

    private fun initiatePossibilityBoard() = run {
        for (y in 1 until Board.BOARD_SIZE) {
            for (x in 1 until Board.BOARD_SIZE) {
                possibilityBoard[y][x] = Cell(x, y)
            }
        }
    }

    fun getBoard(): Array<Array<Int>> {
        return this.board
    }

    fun getCell(x: Int, y: Int): Cell {
        return possibilityBoard[y][x]
    }

    private fun collapseVertical(value: Int, x: Int, y: Int) {
        possibilityBoard.forEachIndexed() { i, data ->
            if (i != y) {
                println("Collapse Vertical: x: $x, y$i")
                data[x].removePossibility(value)
            }
        }
    }

    private fun collapseHorizontal(value: Int, x: Int, y: Int) {
        possibilityBoard[y].forEachIndexed() { i, data ->
            if (i != x) {
                data.removePossibility(value)
            }
        }
    }

    fun getRegion(x: Int, y: Int): Region {
        val positionX: Int = x / 3
        val positionY: Int = y / 3
        val minX = positionX * 3
        val minY = positionY * 3
        return Region(minX = minX, minY = minY)
    }

    private fun collapseRegion(value: Int, x: Int, y: Int) {
//        getRegion => get max and min index, will have minX, minY, since range always+2 of min, inclusive
        val region = getRegion(x = x, y = y)
        println(region)
//        then loop based on that, and remove unintended probability, except the selected cell
        for (i in region.minY..region.minY + 2) {
            for (j in region.minX..region.minX + 2) {
                if (i != y && j != x) {
                    println("Deleting $value: x=$j, y=$i. Current Center: x=$x,y=$y")
                    possibilityBoard[i][j].removePossibility(value)
                }
            }
        }
    }

    fun collapse(value: Int, x: Int, y: Int) {
        println("Collapsing: $value from x: $x, y: $y")
        possibilityBoard[y][x].removeExceptValue(value)
        println(possibilityBoard[y][x].possibilityToString())
//        i = y
//        j = x
//        collapse all possibilityBoard with same element in vertical, horizontal and in 3x3 manner
        collapseVertical(value, x = x, y = y)
        collapseHorizontal(value, x = x, y = y)
        collapseRegion(value, x = x, y = y)
    }

    fun resetPossibilityBoard() {
        possibilityBoard = Array(Board.BOARD_SIZE) { Array(Board.BOARD_SIZE) { Cell() } }
    }

    fun printPossibility() {
        var strData = ""
        possibilityBoard.forEach { row ->
            strData += "| "
            row.forEach { column ->
                strData += "${column.possibilityToString()}|"
            }
            strData += "\n"
        }
        println(strData)
    }

    override fun toString(): String {
        var result = ""
        board.forEachIndexed() { i, data ->
            result += "|"
            data.forEachIndexed() { j, _ ->
                result += " $i,$j |"
            }
            result += "\n"
        }
        return result
    }

    data class Region(val minX: Int, val minY: Int)
}