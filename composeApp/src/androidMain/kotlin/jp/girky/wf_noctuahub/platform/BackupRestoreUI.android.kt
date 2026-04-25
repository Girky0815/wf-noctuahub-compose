package jp.girky.wf_noctuahub.platform

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream

@Composable
actual fun BackupRestoreButtons(
    modifier: Modifier,
    onExport: suspend () -> String,
    onImport: suspend (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // エクスポート用ランチャー
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/x-yaml")
    ) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                try {
                    val content = onExport()
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        outputStream.write(content.toByteArray())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // インポート用ランチャー
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                try {
                    val content = context.contentResolver.openInputStream(it)?.use { inputStream ->
                        inputStream.bufferedReader().use { reader ->
                            reader.readText()
                        }
                    }
                    if (content != null) {
                        onImport(content)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(onClick = {
            exportLauncher.launch("noctuahub_backup.yaml")
        }) {
            Text("設定をエクスポート")
        }

        Button(onClick = {
            // AndroidではYAMLファイルのMIME Type（application/x-yaml や text/yaml）がOS側に認識されない場合が多いため
            // 安全策としてあらゆるファイル（*/*）を選べるようにしています。
            importLauncher.launch(arrayOf("*/*"))
        }) {
            Text("設定をインポート")
        }
    }
}
