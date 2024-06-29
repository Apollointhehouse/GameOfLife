package game

data class State(
    val rows: Int,
    val cols: Int,
    val board: List<List<Cell>>?
)
