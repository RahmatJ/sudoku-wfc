package sudoku

import board.Board

class Cell() {
    private val possibilityValue = mutableListOf<Int>()
    private var entropy: Double = 1.0
    private var x: Int = 0
    private var y: Int = 0

    constructor(x: Int, y: Int) : this() {
        this.x = x
        this.y = y
    }

    init {
        initiatePossibility()
    }

    fun getPossibilityValue(): MutableList<Int> {
        return this.possibilityValue
    }

    fun getX(): Int {
        return x
    }

    fun getY(): Int {
        return y
    }

    fun resetPossibilityValue() {
        initiatePossibility()
    }

    private fun initiatePossibility() {
        for (i in 1..Board.BOARD_SIZE) {
            possibilityValue.add(i)
        }
        updateEntropy()
    }

    fun removePossibility(value: Int) {
        possibilityValue.remove(value)
        updateEntropy()
    }

    fun removeExceptValue(value: Int) {
        if (possibilityValue.size <= 1) {
            return
        }
        if (!possibilityValue.contains(value)) {
            return
        }
        possibilityValue.removeIf { x -> x != value }
        updateEntropy()
    }


    fun getEntropy(): Double {
        return this.entropy
    }

    fun pickRandomElement(): Int {
        return this.possibilityValue.random()
    }

    fun updateEntropy() {
        entropy = possibilityValue.size.toDouble() / Board.BOARD_SIZE.toDouble()
        if (possibilityValue.size <= 1) {
            return
        }

        if (entropy < lowestEntropy) {
            lowestEntropy = entropy
            lowestEntropyCell = this
        }
    }

    fun possibilityToString(): String {
        var result = ""
        for (i in 0 until Board.BOARD_SIZE) {
            if (possibilityValue.size > i) {
                result += "${possibilityValue[i]} "
                continue
            }
            result += "_ "
        }
        return result
    }

    override fun toString(): String {
        return "$x,$y with Entropy $entropy. Possibility: ${possibilityToString()}"
    }

    companion object {
        @JvmStatic
        private var lowestEntropy: Double = 1.0

        @JvmStatic
        private var lowestEntropyCell = Cell(0, 0)

        fun getLowestCell(): Cell {
            println("Lowest Entropy: $lowestEntropy")
            println("Lowest Cell: $lowestEntropyCell")
            return lowestEntropyCell
        }

    }
}