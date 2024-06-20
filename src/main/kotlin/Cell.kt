data class Cell(
    val row: Int,
    val col: Int,
    var neighbours: List<Cell?> = emptyList(),
    var isAlive: Boolean = false
) {
    fun tick() {
        val aliveNeighbours = neighbours.count { it?.isAlive == true }
        when (aliveNeighbours) {
            !in 2..3 -> isAlive = false
            3 -> isAlive = true
        }
    }

    override fun toString(): String {
        return if (isAlive) "X" else "-"
    }
}
