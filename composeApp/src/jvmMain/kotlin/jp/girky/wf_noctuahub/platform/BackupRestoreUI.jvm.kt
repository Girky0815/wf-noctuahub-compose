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
                val fd = FileDialog(null as Frame?, "設定を保存", FileDialog.SAVE)
                fd.file = "noctuahub_backup.yaml"
                fd.isVisible = true
                val file = fd.file
                val dir = fd.directory
                if (file != null && dir != null) {
                    val content = onExport()
                    File(dir, file).writeText(content)
                }
            }
        }) {
            Text("設定をエクスポート")
        }

        Button(onClick = {
            coroutineScope.launch {
                val fd = FileDialog(null as Frame?, "設定をインポート", FileDialog.LOAD)
                fd.isVisible = true
                val file = fd.file
                val dir = fd.directory
                if (file != null && dir != null) {
                    val content = File(dir, file).readText()
                    onImport(content)
                }
            }
        }) {
            Text("設定をインポート")
        }
    }
}
