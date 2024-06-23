data class State(
    val rows: Int = 20,
    val cols: Int = 20,
    val board: List<List<Cell>> = List(rows) { row -> List(cols) { col -> Cell(row, col) } }
)
