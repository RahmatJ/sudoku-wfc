package sudoku

import board.Board

class Sudoku {
    private var board = Array(Board.BOARD_SIZE) { Array(Board.BOARD_SIZE) { 0 } }
    private var possibilityBoard = Array(Board.BOARD_SIZE) { Array(Board.BOARD_SIZE) { Cell() } }

    init {
        initiatePossibilityBoard()
    }

    private fun initiatePossibilityBoard() = run {
        for (y in 0 until Board.BOARD_SIZE) {
            for (x in 0 until Board.BOARD_SIZE) {
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
                val currentCell = data[x]
                currentCell.removePossibility(value)
                if (data[x].getPossibilityValue().size == 1 && !getVisitedCollapse().contains(currentCell)) {
                    addCollapsePending(data[x])
                }
            }
        }
    }

    private fun collapseHorizontal(value: Int, x: Int, y: Int) {
        possibilityBoard[y].forEachIndexed() { i, currentCell ->
            if (i != x) {
                currentCell.removePossibility(value)
                if (currentCell.getPossibilityValue().size == 1 && !getVisitedCollapse().contains(currentCell)) {
                    addCollapsePending(currentCell)
                }
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
                    val currentCell = possibilityBoard[i][j]
                    currentCell.removePossibility(value)
                    if (currentCell.getPossibilityValue().size == 1 && !getVisitedCollapse().contains(currentCell)) {
                        addCollapsePending(currentCell)
                    }
                }
            }
        }
    }

    fun executeCollapse() {
//        TODO(Rahmat): add backtrack logic
        while (getCollapsePendingCount() > 0) {
            val cell = getCollapsePending()
            if (getVisitedCollapse().contains(cell)) {
                println("$cell already visited. Skip")
                continue
            }
            collapse(cell)
            addVisitedCollapse(cell)

            println(getCollapsePendingCount())
            printPossibility()
            readln()
        }
    }

    private fun collapse(cell: Cell) {
        val value = cell.pickRandomElement()
        val x = cell.getX()
        val y = cell.getY()

        println("Collapsing: $value from x: $x, y: $y")
        possibilityBoard[y][x].removeExceptValue(value)
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
        possibilityBoard.forEachIndexed { i, row ->
            strData += "| "
            row.forEachIndexed { j, column ->
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

    companion object {
        @JvmStatic
        private var collapsePending = mutableSetOf<Cell>()

        @JvmStatic
        private var visitedCollapse = mutableSetOf<Cell>()

        fun getCollapsePending(): Cell {
            val result = collapsePending.elementAt(0)
            collapsePending.remove(result)
            println(collapsePending)
            return result
        }

        fun addCollapsePending(cell: Cell) {
            collapsePending.add(cell)
        }

        fun getCollapsePendingCount(): Int {
            return collapsePending.size
        }

        fun addVisitedCollapse(cell: Cell) {
            visitedCollapse.add(cell)
        }

        fun getVisitedCollapse(): MutableSet<Cell> {
            return visitedCollapse
        }
    }
}