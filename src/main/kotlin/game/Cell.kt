package game

data class Cell(
    var isAlive: Boolean = false,
    val pos: Pos,
    val prev: Boolean = false
) {
    private val neighbours = arrayOf(
        Pair(-1 + pos.x, -1 + pos.y),
        Pair(-1 + pos.x, +0 + pos.y),
        Pair(-1 + pos.x, +1 + pos.y),
        Pair(+0 + pos.x, -1 + pos.y),
        Pair(+0 + pos.x, +1 + pos.y),
        Pair(+1 + pos.x, -1 + pos.y),
        Pair(+1 + pos.x, +0 + pos.y),
        Pair(+1 + pos.x, +1 + pos.y)
    )

    fun tick(game: Game): Cell {
        return evolve(game)
    }

    private fun evolve(game: Game): Cell {
        val aliveNeighbours = neighbours.count { (x, y) ->
            game[x, y]?.isAlive == true
        }

        return when (aliveNeighbours) {
            !in 2..3 -> copy(isAlive = false, prev = isAlive)
            3 ->  copy(isAlive = true, prev = isAlive)
            else -> copy(isAlive = isAlive, prev = isAlive)
        }
    }
}