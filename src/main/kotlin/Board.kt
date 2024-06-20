import kotlin.concurrent.thread

class Board(rows: Int, cols: Int) {
    private val cells = List(rows) { row -> List(cols) { col -> Cell(row, col) } }
    private var running = false

    init {
        cells.forEach { row ->
            row.forEach { cell ->
                cell.neighbours = listOf(
                    cells.getOrNull(cell.row - 1)?.getOrNull(cell.col),
                    cells.getOrNull(cell.row - 1)?.getOrNull(cell.col + 1),
                    cells.getOrNull(cell.row - 1)?.getOrNull(cell.col -1),
                    cells.getOrNull(cell.row)?.getOrNull(cell.col + 1),
                    cells.getOrNull(cell.row)?.getOrNull(cell.col - 1),
                    cells.getOrNull(cell.row + 1)?.getOrNull(cell.col),
                    cells.getOrNull(cell.row + 1)?.getOrNull(cell.col + 1),
                    cells.getOrNull(cell.row + 1)?.getOrNull(cell.col - 1)
                )
            }
        }

        cells.forEach { row ->
            row.forEach { cell ->
                cell.isAlive = (0..10).random() == 0
            }
        }
    }

    fun start(): Thread {
        running = true
        return thread {
            while (running) {
                tick()
                Thread.sleep(100)
            }
        }
    }

    fun stop() {
        running = false
    }

    private fun tick() {
        cells.forEach { row ->
            row.forEach { cell ->
                cell.tick()
            }
        }

        for (row in cells) {
            for (cell in row) {
                print(cell)
            }
            println()
        }
        println()
    }
}