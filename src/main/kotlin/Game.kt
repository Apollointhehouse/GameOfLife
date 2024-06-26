import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Game(scope: CoroutineScope, rows: Int, cols: Int, var speed: Int) {
    var running = false
        private set

    private val mutableState = MutableStateFlow(State(rows, cols))
    val state: Flow<State> = mutableState

    init {
        scope.launch {
            newGame()
            start()
        }
    }

    fun newGame(): Game {
        mutableState.update {
            it.copy(
                board = it.board.map { cells ->
                    cells.map {
                        Cell((0..1).random() == 0)
                    }
                }
            )
        }

        return this
    }

    suspend fun start(): Game = coroutineScope {
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

    fun tick() {
        mutableState.update {
            it.copy(
                board = it.board.mapIndexed { row, cells ->
                    cells.mapIndexed { col, cell ->
                        cell.tick(this, row, col)
                    }
                }
            )
        }
    }

    fun cellAt(row: Int, col: Int): Cell? {
        return mutableState.value.board.getOrNull(row)?.getOrNull(col)
    }

    operator fun get(row: Int, col: Int): Cell? {
        return cellAt(row, col)
    }
}