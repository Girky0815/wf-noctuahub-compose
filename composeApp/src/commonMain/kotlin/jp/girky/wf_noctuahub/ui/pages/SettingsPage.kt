package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonGroup
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonOption
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListItem
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.material.icons.filled.Palette

import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.ui.viewmodel.FetchState
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Update
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun SettingsPage(
    appSettings: AppSettings,
    worldState: WorldStateResponse? = null,
    errorMessage: String? = null,
    fetchState: FetchState = FetchState.SUCCESS,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    val themeMode by appSettings.themeModeFlow.collectAsState(ThemeMode.SYSTEM_DEFAULT)
    val seedColorArgb by appSettings.seedColorFlow.collectAsState(0xFF6750A4.toInt())
    val useDynamicColor by appSettings.isDynamicColorFlow.collectAsState(true)

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
                    ThemeMode.LIGHT -> "ライトモード"
                    ThemeMode.DARK -> "ダークモード"
                    ThemeMode.AMOLED_BLACK -> "AMOLED ブラックモード(現在正常動作しません)"
                    ThemeMode.SYSTEM_DEFAULT -> "システムのモードに従う"
                },
                leadingIcon = { Icon(Icons.Default.Palette, contentDescription = null) },
                onClick = { /* TODO: Show dialog? Currently chips below */ }
            )
            
            ListItem {
                ExpressiveButtonGroup(
                    options = listOf(
                        ExpressiveButtonOption(label = "システム", onClick = { coroutineScope.launch { appSettings.setThemeMode(ThemeMode.SYSTEM_DEFAULT) } }),
                        ExpressiveButtonOption(label = "ライト", onClick = { coroutineScope.launch { appSettings.setThemeMode(ThemeMode.LIGHT) } }),
                        ExpressiveButtonOption(label = "ダーク", onClick = { coroutineScope.launch { appSettings.setThemeMode(ThemeMode.DARK) } }),
                        ExpressiveButtonOption(label = "AMOLED", onClick = { coroutineScope.launch { appSettings.setThemeMode(ThemeMode.AMOLED_BLACK) } })
                    ),
                    selectedIndex = when (themeMode) {
                        ThemeMode.SYSTEM_DEFAULT -> 0
                        ThemeMode.LIGHT -> 1
                        ThemeMode.DARK -> 2
                        ThemeMode.AMOLED_BLACK -> 3
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ListTile(
                title = "ダイナミックカラー",
                subtitle = if (useDynamicColor) "OSの配色を使用する" else "固定色を使用する",
                leadingIcon = { Icon(Icons.Default.ColorLens, contentDescription = null) },
                trailingContent = {
                    Switch(
                        checked = useDynamicColor,
                        onCheckedChange = { coroutineScope.launch { appSettings.setDynamicColor(it) } },
                        thumbContent = {
                            if (useDynamicColor) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        }
                    )
                },
                onClick = { coroutineScope.launch { appSettings.setDynamicColor(!useDynamicColor) } }
            )

            if (!useDynamicColor) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                Text(
                    "カスタム配色",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val colors = listOf(
                        Color(0xFFE53935) to "Red",
                        Color(0xFF1E88E5) to "Blue",
                        Color(0xFF43A047) to "Green",
                        Color(0xFF8E24AA) to "Purple",
                        Color(0xFFFFB300) to "Amber",
                        Color(0xFF6750A4) to "Default"
                    )
                    colors.forEach { (color, _) ->
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = MaterialTheme.shapes.small,
                            color = color,
                            onClick = { coroutineScope.launch { appSettings.setSeedColor(color.toArgb()) } },
                            border = if (seedColorArgb == color.toArgb()) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface) else null
                        ) {}
                    }
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

        SectionTitle(title = "API 状態")
        ListGroup {
            val statusText = when (fetchState) {
                FetchState.IDLE -> "待機中"
                FetchState.SUCCESS -> "接続済み"
                FetchState.LOADING_WORLDSTATE, FetchState.LOADING_EXPORT -> "更新中"
                FetchState.ERROR -> "エラー発生"
            }
            ListTile(
                title = "接続状況",
                subtitle = statusText + if (errorMessage != null) " - $errorMessage" else "",
                leadingIcon = { Icon(Icons.Default.Api, contentDescription = null) },
                onClick = null
            )
            
            worldState?.time?.let { timeSec ->
                val dt = Instant.fromEpochSeconds(timeSec).toLocalDateTime(TimeZone.currentSystemDefault())
                val timeStr = "${dt.hour.toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}:${dt.second.toString().padStart(2, '0')}"
                val nowSec = jp.girky.wf_noctuahub.utils.currentTimeMillis() / 1000
                val diffMin = (nowSec - timeSec) / 60
                
                ListTile(
                    title = "WorldState 最終更新",
                    subtitle = "$timeStr (${diffMin}分前)",
                    leadingIcon = { Icon(Icons.Default.Update, contentDescription = null) },
                    onClick = null
                )
            }
            
            ListTile(
                title = "Public Export JSON",
                subtitle = "最新です",
                leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) },
                onClick = null
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
                    title = "Warframe Wiki (日本語)",
                    subtitle = "日本語Wikiを参照する",
                    onClick = { uriHandler.openUri("https://wikiwiki.jp/warframe") }
                )
            }
        }
    }
}
