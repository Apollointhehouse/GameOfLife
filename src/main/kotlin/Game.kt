import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Game(scope: CoroutineScope, rows: Int, cols: Int, speed: Int) {
    private var running = false

    private val mutableState = MutableStateFlow(State(rows, cols))
    val state: Flow<State> = mutableState

    init {
        scope.launch {
            newGame()
            start(speed)
        }
    }

    fun newGame(): Game {
        val board = mutableState.value.board
        val newBoard = board.map { row ->
            row.map { cell ->
                Cell(cell.row, cell.col, (0..10).random() == 0)
            }
        }

        mutableState.update {
            it.copy(
                board = newBoard
            )
        }

        return this
    }

    private suspend fun start(speed: Int): Game = coroutineScope {
        running = true
        while (running) {
            tick()
            delay(1000L / speed)
        }
        this@Game
    }

    fun stop() {
        running = false
    }

    private fun tick() {
        val board = mutableState.value.board
        val newBoard = board.map { row ->
            row.map { cell ->
                cell.tick(this)
            }
        }

        mutableState.update {
            it.copy(
                board = newBoard
            )
        }

        for (row in newBoard) {
            for (cell in row) {
                print(cell)
            }
            println()
        }
        println()
    }
    fun cellAt(row: Int, col: Int): Cell? {
        return mutableState.value.board.getOrNull(row)?.getOrNull(col)
    }
}