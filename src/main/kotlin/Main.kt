import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun App() {
    var text by remember { mutableStateOf("Start Game!") }
    var game: Board? = null

    MaterialTheme {
        Button(onClick = {
            text = "New Game!"

            game?.stop()
            game = Board(20, 20)
            game?.start()


        }) {
            Text(text)
        }

        Box(
            modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)
        )
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
