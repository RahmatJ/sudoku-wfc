package sudoku

class Sudoku {
    private val BOARD_SIZE = 9
    private var board = Array(BOARD_SIZE) { Array(BOARD_SIZE) { 0 } }
    private var possibilityBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { mutableListOf<Int>() } }

    data class Region(val minX: Int, val minY: Int)
    data class Point(val x: Int, val y: Int)

    init {
        initiatePossibilityBoard()
    }

    private fun initiatePossibilityBoard() = run {
        possibilityBoard.forEachIndexed { i, data ->
            data.forEachIndexed { j, _ ->
                val arrayData = mutableListOf<Int>()
                for (element in 1..9) {
                    arrayData.add(element)
                }
                possibilityBoard[i][j] = arrayData
            }

        }
    }

    fun getBoard(): Array<Array<Int>> {
        return this.board
    }

    private fun collapseVertical(value: Int, x: Int, y: Int) {
        possibilityBoard.forEachIndexed() { i, data ->
            if (i != y) {
                data[x].remove(value)
            }
        }
    }

    private fun collapseHorizontal(value: Int, x: Int, y: Int) {
        possibilityBoard[y].forEachIndexed() { i, data ->
            if (i != x) {
                data.remove(value)
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
                    possibilityBoard[i][j].remove(value)
                }
            }
        }
    }

    fun collapse(value: Int, i: Int, j: Int) {
        possibilityBoard[i][j].removeIf { data -> data != value }
//        i = y
//        j = x
//        collapse all possibilityBoard with same element in vertical, horizontal and in 3x3 manner
        collapseVertical(value, x = j, y = i)
        collapseHorizontal(value, x = j, y = i)
        collapseRegion(value, x = j, y = i)
    }

    fun resetPossibilityBoard() {
        possibilityBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { mutableListOf<Int>() } }
    }

    fun printPosiibility() {
        var strData = ""
        possibilityBoard.forEach { row ->
            strData += "|"
            row.forEach { column ->
                column.forEach() {
                    strData += " $it"
                }
                strData += " |"
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
}