import sudoku.Sudoku

fun main(args: Array<String>) {
    val sudoku = Sudoku()
    println(sudoku)
    println("========================")
    sudoku.printPosiibility()
    sudoku.collapse(1, i = 8, j = 8)
    println("========================")
    sudoku.printPosiibility()
}