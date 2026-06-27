package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.repository.AppSettings
import jp.girky.wf_noctuahub.utils.ThemeMode
import jp.girky.wf_noctuahub.utils.AppThemeStyle
import jp.girky.wf_noctuahub.utils.AppThemeContrast
import jp.girky.wf_noctuahub.platform.BackupRestoreButtons
import kotlinx.coroutines.launch
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonGroup
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonOption
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListItem
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.platform.BackHandler
import jp.girky.wf_noctuahub.platform.LibraryVersions

import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.ui.viewmodel.FetchState
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

enum class SettingsSubPage {
  MAIN,
  APPEARANCE,
  DATA_MANAGEMENT,
  DASHBOARD_SETTINGS,
  LICENSE_INFO
}

@Composable
fun SettingsPage(
  appSettings: AppSettings,
  worldState: WorldStateResponse? = null,
  errorMessage: String? = null,
  fetchState: FetchState = FetchState.SUCCESS,
  onNavigateToUpdate: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()
  val coroutineScope = rememberCoroutineScope()
  val uriHandler = LocalUriHandler.current

  var currentSubPage by remember { mutableStateOf(SettingsSubPage.MAIN) }
  var showDeleteConfirmDialog by remember { mutableStateOf(false) }

  // サブページが開いている場合は、戻るボタンでメイン設定画面に戻る
  BackHandler(enabled = currentSubPage != SettingsSubPage.MAIN) {
    currentSubPage = SettingsSubPage.MAIN
  }

  // 設定消去の確認ダイアログ
  if (showDeleteConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showDeleteConfirmDialog = false },
      title = { Text("設定の消去") },
      text = { Text("すべての設定値（補正値、テーマモード、カスタム色など）を初期状態に戻しますか？この操作は取り消せません。") },
      confirmButton = {
        TextButton(
          onClick = {
            coroutineScope.launch {
              appSettings.clearSettings()
              showDeleteConfirmDialog = false
            }
          }
        ) {
          Text("消去する", color = MaterialTheme.colorScheme.error)
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteConfirmDialog = false }) {
          Text("キャンセル")
        }
      }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 疑似サブ画面の場合は、戻るボタン付きのヘッダーを表示
    if (currentSubPage != SettingsSubPage.MAIN) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp)
      ) {
        IconButton(onClick = { currentSubPage = SettingsSubPage.MAIN }) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = when (currentSubPage) {
            SettingsSubPage.APPEARANCE -> "外観設定"
            SettingsSubPage.DATA_MANAGEMENT -> "データ管理"
            SettingsSubPage.DASHBOARD_SETTINGS -> "ダッシュボードの設定"
            SettingsSubPage.LICENSE_INFO -> "ライセンス情報"
            else -> ""
          },
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    when (currentSubPage) {
      SettingsSubPage.MAIN -> {
        // 設定トップ画面
        SectionTitle(title = "設定")
        ListGroup {
          ListTile(
            title = "外観設定",
            subtitle = "テーマモード、カラーテーマの設定",
            leadingIcon = { Icon(Icons.Default.Palette, contentDescription = null) },
            trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
            onClick = { currentSubPage = SettingsSubPage.APPEARANCE }
          )
          ListTile(
            title = "データ管理",
            subtitle = "設定のエクスポート、インポート、消去",
            leadingIcon = { Icon(Icons.Default.Storage, contentDescription = null) },
            trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
            onClick = { currentSubPage = SettingsSubPage.DATA_MANAGEMENT }
          )
          ListTile(
            title = "ダッシュボードの設定",
            subtitle = "ワールドサイクルの手動時間補正",
            leadingIcon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
            trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
            onClick = { currentSubPage = SettingsSubPage.DASHBOARD_SETTINGS }
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
            ListTile(
              title = "WorldState タイムスタンプ",
              subtitle = "$timeStr",
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

        SectionTitle(title = "アプリ概要")
        ListGroup {
          val appUpdater = remember { jp.girky.wf_noctuahub.platform.getAppUpdater() }
          val currentVersionName = remember { appUpdater.getAppVersionName() }

          ListTile(
            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = "Noctua Hub",
            subtitle = "バージョン: $currentVersionName",
            onClick = {}
          )
          ListTile(
            leadingIcon = { Icon(Icons.Default.Update, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = "アップデートを確認",
            subtitle = "新バージョンを確認して適用します",
            onClick = onNavigateToUpdate
          )
          ListTile(
            leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = "GitHub リポジトリ",
            subtitle = "アプリの情報やソースコードを確認する",
            onClick = { uriHandler.openUri("https://github.com/Girky0815/wf-noctuahub-compose") }
          )
          ListTile(
            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = "Warframe Wiki (日本語)",
            subtitle = "日本語Wikiを参照する",
            onClick = { uriHandler.openUri("https://wikiwiki.jp/warframe") }
          )
          ListTile(
            leadingIcon = { Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = "ライセンス情報",
            subtitle = "使用しているオープンソースライブラリのライセンス",
            trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
            onClick = { currentSubPage = SettingsSubPage.LICENSE_INFO }
          )
        }
      }

      SettingsSubPage.APPEARANCE -> {
        // 外観設定サブ画面
        val themeMode by appSettings.themeModeFlow.collectAsState(ThemeMode.SYSTEM_DEFAULT)
        val seedColorArgb by appSettings.seedColorFlow.collectAsState(0xFF6750A4.toInt())
        val useDynamicColor by appSettings.isDynamicColorFlow.collectAsState(true)
        val themeStyle by appSettings.themeStyleFlow.collectAsState(AppThemeStyle.TONAL_SPOT)
        val themeContrast by appSettings.themeContrastFlow.collectAsState(AppThemeContrast.MEDIUM)

        var showStyleDialog by remember { mutableStateOf(false) }
        var showContrastDialog by remember { mutableStateOf(false) }
        var showColorPickerDialog by remember { mutableStateOf(false) }

        // スタイル選択ダイアログ
        if (showStyleDialog) {
          AlertDialog(
            onDismissRequest = { showStyleDialog = false },
            title = { Text("テーマのスタイル") },
            text = {
              Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppThemeStyle.entries.forEach { style ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clickable {
                        coroutineScope.launch {
                          appSettings.setThemeStyle(style)
                          showStyleDialog = false
                        }
                      }
                      .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    RadioButton(
                      selected = themeStyle == style,
                      onClick = {
                        coroutineScope.launch {
                          appSettings.setThemeStyle(style)
                          showStyleDialog = false
                        }
                      }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(style.label)
                  }
                }
              }
            },
            confirmButton = {
              TextButton(onClick = { showStyleDialog = false }) { Text("キャンセル") }
            }
          )
        }

        // コントラスト選択ダイアログ
        if (showContrastDialog) {
          AlertDialog(
            onDismissRequest = { showContrastDialog = false },
            title = { Text("コントラストの設定") },
            text = {
              Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppThemeContrast.entries.forEach { contrast ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clickable {
                        coroutineScope.launch {
                          appSettings.setThemeContrast(contrast)
                          showContrastDialog = false
                        }
                      }
                      .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    RadioButton(
                      selected = themeContrast == contrast,
                      onClick = {
                        coroutineScope.launch {
                          appSettings.setThemeContrast(contrast)
                          showContrastDialog = false
                        }
                      }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(contrast.label)
                  }
                }
              }
            },
            confirmButton = {
              TextButton(onClick = { showContrastDialog = false }) { Text("キャンセル") }
            }
          )
        }

        // カラー選択ツール（Android Canary 2606 風）ダイアログ
        if (showColorPickerDialog) {
          val initialColor = Color(seedColorArgb.toLong())
          var currentHue by remember { mutableStateOf(jp.girky.wf_noctuahub.utils.ColorUtils.colorToHue(initialColor)) }
          val selectedPickerColor = remember(currentHue) { jp.girky.wf_noctuahub.utils.ColorUtils.hsvToColor(currentHue) }

          AlertDialog(
            onDismissRequest = { showColorPickerDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = true),
            confirmButton = {},
            title = null,
            text = {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    "色を選択する",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  IconButton(
                    onClick = { showColorPickerDialog = false },
                    modifier = Modifier.size(36.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = "閉じる",
                      tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }

                val rainbowColors = listOf(
                  Color(0xFFFF0000),
                  Color(0xFFFFB300),
                  Color(0xFF00FF00),
                  Color(0xFF00FFFF),
                  Color(0xFF0000FF),
                  Color(0xFFFF00FF),
                  Color(0xFFFF0000)
                )

                @OptIn(ExperimentalMaterial3Api::class)
                Slider(
                  value = currentHue,
                  onValueChange = { currentHue = it },
                  valueRange = 0f..360f,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                  thumb = {
                    Surface(
                      modifier = Modifier
                        .size(36.dp)
                        .shadow(4.dp, shape = androidx.compose.foundation.shape.CircleShape),
                      shape = androidx.compose.foundation.shape.CircleShape,
                      color = selectedPickerColor,
                      border = androidx.compose.foundation.BorderStroke(3.dp, Color.White)
                    ) {}
                  },
                  track = {
                    Box(
                      modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(androidx.compose.ui.graphics.Brush.horizontalGradient(rainbowColors))
                    )
                  }
                )

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  FilledIconButton(
                    onClick = { showColorPickerDialog = false },
                    colors = IconButtonDefaults.filledIconButtonColors(
                      containerColor = MaterialTheme.colorScheme.surfaceVariant,
                      contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.size(56.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = "キャンセル",
                      modifier = Modifier.size(24.dp)
                    )
                  }

                  FilledIconButton(
                    onClick = {
                      coroutineScope.launch {
                        appSettings.setSeedColor(selectedPickerColor.toArgb())
                        showColorPickerDialog = false
                      }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                      containerColor = selectedPickerColor,
                      contentColor = if (currentHue in 60f..180f) Color.Black else Color.White
                    ),
                    modifier = Modifier.size(56.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "確定",
                      modifier = Modifier.size(24.dp)
                    )
                  }
                }
              }
            }
          )
        }

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
            onClick = null
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

          ListTile(
            title = "テーマのスタイル",
            subtitle = themeStyle.label,
            leadingIcon = { Icon(Icons.Default.Palette, contentDescription = null) },
            onClick = { showStyleDialog = true }
          )

          ListTile(
            title = "コントラスト設定",
            subtitle = themeContrast.label,
            leadingIcon = { Icon(Icons.Default.ColorLens, contentDescription = null) },
            onClick = { showContrastDialog = true }
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

              // 自由色選択ダイアログを開くための + ボタン
              val isCustomColorSelected = colors.none { it.first.toArgb() == seedColorArgb }
              Surface(
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.small,
                color = if (isCustomColorSelected) Color(seedColorArgb.toLong()) else MaterialTheme.colorScheme.surfaceVariant,
                onClick = { showColorPickerDialog = true },
                border = if (isCustomColorSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface) else null
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "色をカスタマイズ",
                    tint = if (isCustomColorSelected) {
                      val hue = jp.girky.wf_noctuahub.utils.ColorUtils.colorToHue(Color(seedColorArgb.toLong()))
                      if (hue in 60f..180f) Color.Black else Color.White
                    } else {
                      MaterialTheme.colorScheme.onSurfaceVariant
                    }
                  )
                }
              }
            }
          }
        }
      }

      SettingsSubPage.DATA_MANAGEMENT -> {
        // データ管理サブ画面
        ListGroup {
          BackupRestoreButtons(
            onExport = {
              val map = appSettings.exportSettingsAsMap()
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

          HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

          ListTile(
            title = "設定の消去",
            subtitle = "アプリのすべての設定・調整値を初期化します",
            leadingIcon = { Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            onClick = { showDeleteConfirmDialog = true }
          )
        }
      }

      SettingsSubPage.DASHBOARD_SETTINGS -> {
        // ダッシュボードの設定サブ画面
        val cetusOffset by appSettings.cetusOffsetFlow.collectAsState(0)
        val vallisOffset by appSettings.vallisOffsetFlow.collectAsState(0)

        // 説明書きのテキスト
        ListItem(
          containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "ワールドサイクルの手動調整",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(bottom = 4.dp)
          )
          Text(
            text = "ゲーム内のサイクル残り時間とアプリの表示がずれている場合、ここで手動補正を行うことができます。\n\n" +
                   "・アプリの表示がゲーム内より【長い（進んでいる）】場合は「減らす」方向（-秒）へ調整してください。\n" +
                   "・アプリの表示がゲーム内より【短い（遅れている）】場合は「増やす」方向（+秒）へ調整してください。\n\n" +
                   "※エイドロンの草原とカンビオン荒地は共通の補正値が適用されます。\n" +
                   "※補正設定は設定のエクスポート/インポートからバックアップできます。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
          )
        }

        ListGroup {
          ListTile(
            title = "エイドロン / カンビオン 調整",
            subtitle = "現在の補正値: ${if (cetusOffset >= 0) "+" else ""}${cetusOffset}秒",
            leadingIcon = { Icon(Icons.Default.Update, contentDescription = null) },
            onClick = null
          )
          ListItem {
            Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              listOf(-60, -10, -1, 1, 10, 60).forEach { delta ->
                val label = if (delta > 0) "+${delta}s" else "${delta}s"
                Button(
                  onClick = { coroutineScope.launch { appSettings.setCetusOffset(cetusOffset + delta) } },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                  ),
                  contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                  modifier = Modifier.weight(1f).height(32.dp)
                ) {
                  Text(text = label, style = MaterialTheme.typography.labelSmall)
                }
              }
            }
          }

          ListTile(
            title = "オーブ峡谷 調整",
            subtitle = "現在の補正値: ${if (vallisOffset >= 0) "+" else ""}${vallisOffset}秒",
            leadingIcon = { Icon(Icons.Default.Update, contentDescription = null) },
            onClick = null
          )
          ListItem {
            Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              listOf(-60, -10, -1, 1, 10, 60).forEach { delta ->
                val label = if (delta > 0) "+${delta}s" else "${delta}s"
                Button(
                  onClick = { coroutineScope.launch { appSettings.setVallisOffset(vallisOffset + delta) } },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                  ),
                  contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                  modifier = Modifier.weight(1f).height(32.dp)
                ) {
                  Text(text = label, style = MaterialTheme.typography.labelSmall)
                }
              }
            }
          }

          ListItem {
            Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Button(
                onClick = {
                  coroutineScope.launch {
                    appSettings.setCetusOffset(31)
                    appSettings.setVallisOffset(801)
                  }
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = MaterialTheme.colorScheme.primaryContainer,
                  contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.weight(1f)
              ) {
                Text("おすすめ値に設定", style = MaterialTheme.typography.labelMedium)
              }

              OutlinedButton(
                onClick = {
                  coroutineScope.launch {
                    appSettings.setCetusOffset(0)
                    appSettings.setVallisOffset(0)
                  }
                },
                modifier = Modifier.weight(1f)
              ) {
                Text("リセット", style = MaterialTheme.typography.labelMedium)
              }
            }
          }
        }
      }

      SettingsSubPage.LICENSE_INFO -> {
        // ライセンス情報サブ画面 (グループ化 & バージョン明示)
        SectionTitle(title = "フレームワーク & 言語")
        ListGroup {
          ListTile(title = "Kotlin / Kotlin Multiplatform (v${LibraryVersions.KOTLIN})", subtitle = "Apache License 2.0\nhttps://github.com/JetBrains/kotlin", onClick = null)
          ListTile(title = "Compose Multiplatform (v${LibraryVersions.COMPOSE})", subtitle = "Apache License 2.0\nhttps://github.com/JetBrains/compose-multiplatform", onClick = null)
          ListTile(title = "Android Gradle Plugin (v${LibraryVersions.AGP})", subtitle = "Apache License 2.0\nhttps://developer.android.com/studio/build", onClick = null)
        }

        Spacer(modifier = Modifier.height(8.dp))
        SectionTitle(title = "ネットワーク & シリアライズ")
        ListGroup {
          ListTile(title = "Ktor HTTP Client (v${LibraryVersions.KTOR})", subtitle = "Apache License 2.0\nhttps://github.com/ktorio/ktor", onClick = null)
          ListTile(title = "Kotlinx Serialization (v${LibraryVersions.SERIALIZATION})", subtitle = "Apache License 2.0\nhttps://github.com/Kotlin/kotlinx.serialization", onClick = null)
          ListTile(title = "Kotlinx Datetime (v${LibraryVersions.DATETIME})", subtitle = "Apache License 2.0\nhttps://github.com/Kotlin/kotlinx-datetime", onClick = null)
          ListTile(title = "Yamlkt (v${LibraryVersions.YAMLKT})", subtitle = "Apache License 2.0\nhttps://github.com/him188/yamlkt", onClick = null)
        }

        Spacer(modifier = Modifier.height(8.dp))
        SectionTitle(title = "ユーティリティ")
        ListGroup {
          ListTile(title = "Multiplatform Settings (v${LibraryVersions.SETTINGS})", subtitle = "Apache License 2.0\nhttps://github.com/russhwolf/multiplatform-settings", onClick = null)
          ListTile(title = "Material Kolor (v${LibraryVersions.MATERIAL_KOLOR})", subtitle = "Apache License 2.0\nhttps://github.com/MaterialKolor/Input", onClick = null)
          ListTile(title = "JNA (v${LibraryVersions.JNA})", subtitle = "Apache License 2.0 / LGPL 2.1\nhttps://github.com/java-native-access/jna", onClick = null)
          ListTile(title = "XZ for Java (v${LibraryVersions.TUKAANI_XZ})", subtitle = "Public Domain\nhttps://tukaani.org/xz/java.html", onClick = null)
        }

        Spacer(modifier = Modifier.height(8.dp))
        SectionTitle(title = "アセット & UIリファレンス")
        ListGroup {
          ListTile(title = "Material Symbols Rounded", subtitle = "Apache License 2.0\nhttps://github.com/google/material-design-icons", onClick = null)
          ListTile(title = "Google Sans Flex Font", subtitle = "SIL Open Font License 1.1\nhttps://fonts.google.com", onClick = null)
          ListTile(title = "Noto Sans JP Font", subtitle = "SIL Open Font License 1.1\nhttps://fonts.google.com/noto", onClick = null)
          ListTile(title = "UI Reference: Tomato", subtitle = "MIT License\nhttps://github.com/nsh07/Tomato", onClick = null)
        }
      }
    }
  }
}
