package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.repository.AppSettings
import jp.girky.wf_noctuahub.utils.ThemeMode
import jp.girky.wf_noctuahub.platform.BackupRestoreButtons
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.material.icons.filled.Palette

@Composable
fun SettingsPage(
    appSettings: AppSettings,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    val themeMode by appSettings.themeModeFlow.collectAsState(ThemeMode.SYSTEM_DEFAULT)
    val seedColorArgb by appSettings.seedColorFlow.collectAsState(0xFF6750A4.toInt())

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionTitle(title = "外観")
        ListGroup {
            ListTile(
                title = "テーマモード",
                subtitle = when (themeMode) {
                    ThemeMode.LIGHT -> "ライト"
                    ThemeMode.DARK -> "ダーク"
                    ThemeMode.AMOLED_BLACK -> "AMOLED ブラック"
                    ThemeMode.SYSTEM_DEFAULT -> "システム設定に従う"
                },
                leadingIcon = { Icon(Icons.Default.Palette, contentDescription = null) },
                onClick = { /* TODO: Show dialog */ }
            )
            
            // 簡易的なテーマ切り替え(テスト用)
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeMode.entries.forEach { mode ->
                    FilterChip(
                        selected = themeMode == mode,
                        onClick = { coroutineScope.launch { appSettings.setThemeMode(mode) } },
                        label = { Text(mode.name.lowercase().replace("_", " ")) }
                    )
                }
            }

            SectionTitle(title = "配色設定")
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val colors = listOf(
                    Color(0xFFE53935) to "Red",
                    Color(0xFF1E88E5) to "Blue",
                    Color(0xFF43A047) to "Green",
                    Color(0xFF8E24AA) to "Purple",
                    Color(0xFFFFB300) to "Amber"
                )
                colors.forEach { (color, label) ->
                    Button(
                        onClick = { coroutineScope.launch { appSettings.setSeedColor(color.toArgb()) } },
                        colors = ButtonDefaults.buttonColors(containerColor = color)
                    ) { Text(label, color = Color.White) }
                }
            }
        }

        SectionTitle(title = "データ管理")
        ListGroup {
            BackupRestoreButtons(
                onExport = {
                    val map = appSettings.exportSettingsAsMap()
                    // YAML形式にするための簡易処理(本来はyamlkt使用)
                    val yaml = map.entries.joinToString("\n") { "${it.key}: ${it.value}" }
                    yaml
                },
                onImport = { yaml ->
                    val map = yaml.lines().filter { it.contains(":") }.associate { 
                        val parts = it.split(":")
                        parts[0].trim() to parts[1].trim()
                    }
                    appSettings.importSettingsFromMap(map)
                }
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionTitle(title = "概要")
            ListGroup {
                ListTile(
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    title = "Noctua Hub (Compose Desktop)",
                    subtitle = "バージョン: 1.0.0-dev",
                    onClick = {}
                )
                ListTile(
                    leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    title = "GitHub リポジトリ",
                    subtitle = "アプリの情報やソースコードを確認する",
                    onClick = { uriHandler.openUri("https://github.com/Girky0815/wf-noctua-hub") }
                )
                ListTile(
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    title = "Warframe Wiki (英語)",
                    subtitle = "公式のデータを参照する",
                    onClick = { uriHandler.openUri("https://warframe.fandom.com/wiki/WARFRAME_Wiki") }
                )
            }
        }
    }
}
