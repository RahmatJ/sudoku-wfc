import board.Board
import sudoku.Cell
import sudoku.Sudoku

fun main(args: Array<String>) {
    val sudoku = Sudoku()
    println(sudoku)
    println("========================")
    sudoku.printPossibility()
    var count = 0
    while (true) {
        var cell: Cell
        val x = (0 until Board.BOARD_SIZE).random()
        val y = (0 until Board.BOARD_SIZE).random()
        cell = sudoku.getCell(x = x, y = y)


        if (cell.getPossibilityValue().size == 1) {
            continue
        }

        println(cell)

        val pickedPossibility = cell.pickRandomElement()

        sudoku.collapse(pickedPossibility, x = cell.getX(), y = cell.getY())

        println("Count: $count")
        sudoku.printPossibility()
        println("========================")

        if (count >= 100) {
            break
        }

        count++
    }
}