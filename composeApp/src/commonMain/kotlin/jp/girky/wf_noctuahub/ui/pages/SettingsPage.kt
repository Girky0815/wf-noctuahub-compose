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
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle

@Composable
fun SettingsPage(
    isDark: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    seedColor: Color,
    onSeedColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // デバック/開発中の暫定テーマ設定 UI
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionTitle(title = "外観設定")
            ListGroup {
                ListTile(
                    leadingIcon = { Icon(Icons.Default.ColorLens, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    title = "ダークモード",
                    subtitle = "アプリの配色を暗くします",
                    trailingContent = {
                        Switch(checked = isDark, onCheckedChange = onDarkThemeChange)
                    }
                )
                // 色変更ボタンの一時的な配置
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onSeedColorChange(Color(0xFFE53935)) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                    ) { Text("Red") }
                    Button(
                        onClick = { onSeedColorChange(Color(0xFF1E88E5)) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                    ) { Text("Blue") }
                    Button(
                        onClick = { onSeedColorChange(Color(0xFF43A047)) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                    ) { Text("Green") }
                }
            }
        }

        // 概要
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionTitle(title = "概要")
            ListGroup {
                ListTile(
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    title = "Noctua Hub (Compose Desktop)",
                    subtitle = "バージョン: 開発中",
                    onClick = {}
                )
                ListTile(
                    leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    title = "GitHub リポジトリ",
                    subtitle = "アプリの情報やソースコードを確認する",
                    onClick = { /* TODO: URIを開く */ }
                )
            }
        }
    }
}
