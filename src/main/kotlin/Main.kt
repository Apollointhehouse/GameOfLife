import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlin.math.sign

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun App() {
    var scale by remember { mutableStateOf(5f) }
    var paused by remember { mutableStateOf(false) }
    var speed by remember { mutableStateOf(50f) }
    var pan by remember { mutableStateOf(Offset(0f, 0f)) }

    val scope = rememberCoroutineScope()
    val game = Game(scope, 200, 200, speed = 50)
    val state = game.state.collectAsState(null)

    MaterialTheme {
        Scaffold(
            modifier = Modifier
                .padding(20.dp)
                .onPointerEvent(PointerEventType.Scroll) {
                    val change = it.changes.first()
                    val delta = change.scrollDelta.y.toInt().sign
                    scale = (scale - delta).coerceIn(1f, 40f)
                }
                .onPointerEvent(PointerEventType.Move) {
                    val change = it.changes.first()
                    pan += change.positionChange()
                },
            topBar = {
                TopAppBar(
                    backgroundColor = Color.LightGray,
                    title = { Text("Game of Life") },
                    actions = {
                        Button(
                            onClick = {
                                game.newGame()
                            },
                            modifier = Modifier
                        ) {
                            Text("New Game!")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                paused = !paused
                                if (game.running) {
                                    game.stop()
                                } else {
                                    scope.launch { game.start() }
                                }
                            },
                            modifier = Modifier
                        ) {
                            Text(if (paused) "Resume" else "Pause")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                game.tick()
                            },
                            modifier = Modifier
                        ) {
                            Text("Step")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Slider(
                        value = speed,
                        onValueChange = {
                            speed = it
                            game.speed = speed.toInt()
                        },
                        valueRange = 5f..100f,
                    )
                }


                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(-1f)
                        .offset(pan.x.dp, pan.y.dp)
                ) {
                    val (width, height) = size
                    val (rows, cols) = state.value ?: return@Canvas

                    drawRect(
                        Color.DarkGray,
                        Offset(width / 2 - (cols * scale) / 2 , height / 2 - (rows * scale) / 2),
                        Size(cols * scale, rows * scale),
                        style = Stroke(0.3f)
                    )

                    for (x in 0 until cols) {
                        for (y in 0 until rows) {
                            if (game[x, y]?.isAlive != true) continue
                            drawRect(
                                Color.Black,
                                Offset((width / 2 - (cols * scale) / 2 + x * scale), (height / 2 - (rows * scale) / 2 + y * scale)),
                                Size(0.8f * scale, 0.8f * scale)
                            )
                        }
                    }
                }
            }
        )
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
