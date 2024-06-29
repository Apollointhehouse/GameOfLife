package game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Game(
    scope: CoroutineScope,
    private val rows: Int,
    private val cols: Int,
    var speed: Int
) {
    var running = false
        private set

    private val mutableState = MutableStateFlow(State(rows, cols, null))
    val state: Flow<State> = mutableState

    init {
        scope.launch {
            newGame()
            start()
        }
    }

    fun newGame(): List<List<Cell>> {
        val board = List(rows) { y ->
            List(cols) { x ->
                Cell((0..1).random() == 0, Pos(x to y))
            }
        }

        mutableState.update { it.copy(board = board) }
        return board
    }

    suspend fun start(): Game {
        running = true
        while (running) {
            tick()
            delay(1000L / speed)
        }
        return this
    }

    fun stop() {
        running = false
    }

    fun tick() {
        mutableState.update {
            it.copy(
                board = it.board?.map { cells ->
                    cells.map { cell ->
                        cell.tick(this)
                    }
                }
            )
        }
    }

    operator fun get(x: Int, y: Int): Cell? =
        mutableState.value.board?.getOrNull(y)?.getOrNull(x)

}