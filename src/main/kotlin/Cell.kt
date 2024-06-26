@JvmInline
value class Cell(val isAlive: Boolean = false) {
    private val neighbours: Array<Pair<Int, Int>>
        get() = arrayOf(
            Pair(-1, -1),
            Pair(-1, 0),
            Pair(-1, 1),
            Pair(0, -1),
            Pair(0, 1),
            Pair(1, -1),
            Pair(1, 0),
            Pair(1, 1)
        )

    fun tick(game: Game, row: Int, col: Int): Cell {
        val neighbours = neighbours.map { (rowOffset, colOffset) ->
            Pair(row + rowOffset, col + colOffset)
        }

        val aliveNeighbours = neighbours.count { (row, col) ->
            game.cellAt(row, col)?.isAlive == true
        }

        return when (aliveNeighbours) {
            !in 2..3 -> Cell(false)
            3 ->  Cell(true)
            else -> this
        }
    }
}