package jp.girky.wf_noctuahub.platform

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun BackupRestoreButtons(
    modifier: Modifier,
    onExport: suspend () -> String,
    onImport: suspend (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(onClick = {
            coroutineScope.launch {
                val content = onExport()
                val dialog = FileDialog(null as Frame?, "設定をエクスポート", FileDialog.SAVE).apply {
                    file = "noctuahub_backup.yaml"
                    isVisible = true
                }
                if (dialog.directory != null && dialog.file != null) {
                    val file = File(dialog.directory, dialog.file)
                    file.writeText(content)
                }
            }
        }) {
            Text("設定をエクスポート")
        }

        Button(onClick = {
            coroutineScope.launch {
                val dialog = FileDialog(null as Frame?, "設定をインポート", FileDialog.LOAD).apply {
                    file = "*.yaml;*.yml"
                    isVisible = true
                }
                if (dialog.directory != null && dialog.file != null) {
                    val file = File(dialog.directory, dialog.file)
                    if (file.exists()) {
                        val content = file.readText()
                        onImport(content)
                    }
                }
            }
        }) {
            Text("設定をインポート")
        }
    }
}
