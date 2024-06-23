data class Cell(
    val row: Int,
    val col: Int,
    val isAlive: Boolean = false
) {
    private val neighbours = (1..3).flatMap { row ->
        (1..3).map { col ->
            if (row == 2 && col == 2) null
            else Pair(row + this.row - 2, col + this.col - 2)
        }
    }.filterNotNull()

    fun tick(game: Game): Cell {
        val aliveNeighbours = neighbours.count {
            val (row, col) = it
            game.cellAt(row, col)?.isAlive == true
        }
        return when (aliveNeighbours) {
            !in 2..3 -> Cell(row, col, false)
            3 ->  Cell(row, col, true)
            else -> return this
        }
    }

    override fun toString(): String {
        return if (isAlive) "X" else "-"
    }
}
