package jp.girky.wf_noctuahub

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Noctua Hub",
    ) {
        App()
    }
}