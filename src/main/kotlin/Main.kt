import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun App() {
    var text by remember { mutableStateOf("Start Game!") }
    var scale by remember { mutableStateOf(20f) }

    val scope = rememberCoroutineScope()
    val game = Game(scope, 20, 20, speed = 20)
    val state = game.state.collectAsState(State())

    MaterialTheme {
        Button(onClick = {
            text = "New Game!"

            game.newGame()

        }) {
            Text(text)
        }

        Slider(
            value = scale,
            onValueChange = { scale = it },
            valueRange = 10f..40f,
            modifier = Modifier.offset(y = 50.dp)
        )

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val (width, height) = size
            val (rows, cols) = state.value

            drawRect(
                Color.DarkGray,
                Offset(width / 2 - (rows * scale) / 2, height / 2 - (cols * scale) / 2),
                Size(rows * scale, cols * scale),
                style = Stroke(0.3f)
            )

            for (x in 0 until 20) {
                for (y in 0 until 20) {
                    if (game.cellAt(x, y)?.isAlive != true) continue
                    drawRect(
                        Color.Black,
                        Offset((width / 2 - (rows * scale) / 2 + x * scale), (height / 2 - (cols * scale) / 2 + y * scale)),
                        Size(0.8f * scale, 0.8f * scale)
                    )
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
