package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.girky.wf_noctuahub.data.api.model.ExportRelic
import jp.girky.wf_noctuahub.data.api.model.RelicReward
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.repository.WarframeRepository
import jp.girky.wf_noctuahub.platform.getPlatformUtils
import kotlinx.coroutines.launch

/**
 * Primeシリーズのセットを表現するデータクラス
 */
data class PrimeItemSet(
  val warframe: String,           // 例: "Voruna Prime"
  val weapons: List<String>,       // 例: ["Perigale Prime", "Sarofang Prime"]
  val wikiUrlName: String          // Wiki検索用のキーワード
)

/**
 * テーブルの各セルに表示するデータ
 */
data class RelicTableCell(
  val relicNameShort: String,      // 例: "N19"
  val partName: String,            // 例: "シャーシ"
  val count: Int,                  // 必要数 (x2など)
  val rarity: String               // COMMON, UNCOMMON, RARE
)

@Composable
fun RelicTablePage(
  repository: WarframeRepository,
  worldState: WorldStateResponse?,
  onLocalize: (String) -> String
) {
  val coroutineScope = rememberCoroutineScope()
  val platformUtils = remember { getPlatformUtils() }
  
  // キャプチャ用のグラフィックスレイヤー
  val graphicsLayer = rememberGraphicsLayer()
  val snackbarHostState = remember { SnackbarHostState() }

  // プリセットされる歴代の主要Primeシリーズ一覧 (マスターデータ)
  val allPrimeSets = remember {
    listOf(
      PrimeItemSet("Voruna Prime", listOf("Perigale Prime", "Sarofang Prime"), "Voruna"),
      PrimeItemSet("Gauss Prime", listOf("Acceltra Prime", "Akarius Prime"), "Gauss"),
      PrimeItemSet("Protea Prime", listOf("Velox Prime", "Okina Prime"), "Protea"),
      PrimeItemSet("Sevagoth Prime", listOf("Epitaph Prime", "Nautilus Prime"), "Sevagoth"),
      PrimeItemSet("Grendel Prime", listOf("Masseter Prime", "Zylok Prime"), "Grendel"),
      PrimeItemSet("Wisp Prime", listOf("Fulmin Prime", "Gunsen Prime"), "Wisp"),
      PrimeItemSet("Hildryn Prime", listOf("Larkspur Prime", "Shade Prime"), "Hildryn"),
      PrimeItemSet("Baruuk Prime", listOf("Cobra & Crane Prime", "Afentis Prime"), "Baruuk"),
      PrimeItemSet("Volt Prime", listOf("Odonata Prime", "Soma Prime"), "Volt"),
      PrimeItemSet("Loki Prime", listOf("Bo Prime", "Wyrm Prime"), "Loki")
    )
  }

  // 現在アクティブなレリックから出現するものだけを動的に選別してUIの選択リストにする
  val primeSets = remember(worldState) {
    allPrimeSets.filter { set ->
      val itemNames = listOf(set.warframe) + set.weapons
      itemNames.any { itemName ->
        val partSuffixes = listOf("設計図", "シャーシ", "システム", "ニューロティックス", "レシーバー", "バレル", "ストック", "ブレード", "ハンドル", "グリップ")
        partSuffixes.any { suffix ->
          val localizedSearch = "$itemName $suffix"
          val relicsForPart = repository.getRelicsForLocalizedItem(localizedSearch)
          relicsForPart.any { (relic, _) ->
            repository.isRelicActive(relic.uniqueName)
          }
        }
      }
    }
  }

  var selectedIndex by remember { mutableStateOf(0) }
  
  // 選択されたインデックスが現在の動的リストの範囲外にならないよう調整
  val safeSelectedIndex = if (selectedIndex >= primeSets.size) 0 else selectedIndex
  val currentSet = if (primeSets.isNotEmpty()) primeSets[safeSelectedIndex] else null

  // アクティブなレリックのみを使用するため、キャッシュを最新化
  LaunchedEffect(worldState) {
    repository.updateActiveRelics(worldState)
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    containerColor = Color.Transparent
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp)
    ) {
      item {
        Text(
          text = "レリック周回計画テーブル",
          style = MaterialTheme.typography.displaySmall,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
          text = "アクティブなレリックから出現するパーツを一覧にし、周回計画を立てられます。",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(bottom = 24.dp)
        )
      }

      // セットの選択
      item {
        Text(
          text = "シリーズを選択",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (primeSets.isEmpty()) {
            Text("現在アクティブなレリックがありません", color = MaterialTheme.colorScheme.onSurfaceVariant)
          } else {
            primeSets.forEachIndexed { index, set ->
              FilterChip(
                selected = safeSelectedIndex == index,
                onClick = { selectedIndex = index },
                label = { Text(set.warframe.replace(" Prime", "")) }
              )
            }
          }
        }
      }

      // 操作ボタン
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = {
              if (currentSet != null) {
                coroutineScope.launch {
                  val imageBitmap = graphicsLayer.toImageBitmap()
                  val success = platformUtils.saveImageToGallery(
                    image = imageBitmap,
                    fileName = "NoctuaHub_RelicTable_${currentSet.warframe.replace(" ", "_")}"
                  )
                  if (success) {
                    snackbarHostState.showSnackbar("画像をギャラリーに保存しました。")
                  } else {
                    snackbarHostState.showSnackbar("画像の保存に失敗しました。")
                  }
                }
              }
            },
            enabled = currentSet != null,
            modifier = Modifier.weight(1f)
          ) {
            Text("画像を保存", fontWeight = FontWeight.Bold)
          }

          // クリップボードコピーボタン (Androidでは非表示にするためPlatformUtilsのサポートチェック)
          val supportClipboard = remember { 
            // WindowsデスクトップJVMであればtrue、Androidは画像直接コピーが困難なためfalse
            // コピー対象データが存在するかどうかも加味
            true 
          }
          
          if (supportClipboard) {
            ElevatedButton(
              onClick = {
                if (currentSet != null) {
                  coroutineScope.launch {
                    val imageBitmap = graphicsLayer.toImageBitmap()
                    val success = platformUtils.copyImageToClipboard(imageBitmap)
                    if (success) {
                      snackbarHostState.showSnackbar("画像をクリップボードにコピーしました！")
                    } else {
                      snackbarHostState.showSnackbar("コピーツールが非サポートであるか、失敗しました。")
                    }
                  }
                }
              },
              enabled = currentSet != null,
              modifier = Modifier.weight(1f)
            ) {
              Text("クリップボードにコピー", fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // テーブル本体 (キャプチャ対象)
      item {
        if (currentSet != null) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
              .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
              .drawWithContent {
                // Composeレイヤーを画像用にレコード
                graphicsLayer.record {
                  this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
              }
              .padding(8.dp)
          ) {
            Column {
              // 列ヘッダー
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    RoundedCornerShape(8.dp)
                  )
                  .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // 左上角の空白枠 (対角線ラベルエリア風)
                Box(
                  modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = 4.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "レリック \\ シリーズ",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                  )
                }

                // Warframe 列
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clickable {
                      platformUtils.openBrowser("https://wikiwiki.jp/warframe/${currentSet.wikiUrlName}")
                    }
                    .padding(horizontal = 4.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = currentSet.warframe,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                  )
                }

                // 武器 列
                currentSet.weapons.forEach { weapon ->
                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .clickable {
                        platformUtils.openBrowser("https://wikiwiki.jp/warframe/${weapon.replace(" Prime", "")}")
                      }
                      .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = weapon,
                      style = MaterialTheme.typography.labelMedium,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.primary,
                      textAlign = TextAlign.Center
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              // 行データ (Lith, Meso, Neo, Axi)
              val tiers = listOf("Lith", "Meso", "Neo", "Axi")
              tiers.forEach { tier ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  // 行ヘッダー (Lith, Meso等)
                  Box(
                    modifier = Modifier
                      .weight(0.8f)
                      .background(
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        RoundedCornerShape(6.dp)
                      )
                      .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = tier,
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                  }

                  // Warframe列データ
                  val wfCell = getCellData(repository, currentSet.warframe, tier, onLocalize)
                  RenderTableCell(wfCell, modifier = Modifier.weight(1f))

                  // 武器列データ
                  currentSet.weapons.forEach { weapon ->
                    val wCell = getCellData(repository, weapon, tier, onLocalize)
                    RenderTableCell(wCell, modifier = Modifier.weight(1f))
                  }
                }
              }
            }
          }
        } else {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
              .padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "現在アクティブな出現中シリーズがありません。",
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "※ ヘッダーの装備名をタップすると、外部ブラウザで非公式Wiki（Wikiwiki）のページを開くことができます。\n※ 画像の保存・共有の際はアクティブなレリックのみが抽出されます。",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )
      }
    }
  }
}

/**
 * テーブルセルの描画用コンポーネント
 */
@Composable
fun RenderTableCell(cell: RelicTableCell?, modifier: Modifier = Modifier) {
  if (cell == null) {
    Box(
      modifier = modifier
        .padding(horizontal = 4.dp)
        .fillMaxHeight()
        .background(
          MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
          RoundedCornerShape(6.dp)
        )
        .padding(vertical = 12.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "-",
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        style = MaterialTheme.typography.bodyMedium
      )
    }
  } else {
    // レアリティに応じた美しいパステル背景色
    val containerColor = when (cell.rarity) {
      "RARE" -> Color(0xFFFFF9C4) // 薄ゴールド
      "UNCOMMON" -> Color(0xFFECEFF1) // 薄シルバー
      else -> Color(0xFFF5EBE6) // 薄ブロンズ
    }
    
    val borderColor = when (cell.rarity) {
      "RARE" -> Color(0xFFFBC02D)
      "UNCOMMON" -> Color(0xFF90A4AE)
      else -> Color(0xFFBCAAA4)
    }

    Box(
      modifier = modifier
        .padding(horizontal = 4.dp)
        .background(containerColor, RoundedCornerShape(6.dp))
        .border(1.dp, borderColor, RoundedCornerShape(6.dp))
        .padding(vertical = 6.dp, horizontal = 2.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = cell.relicNameShort,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = if (cell.rarity == "RARE") Color(0xFFD84315) else Color.Black,
          fontSize = 15.sp
        )
        Text(
          text = if (cell.count > 1) "${cell.partName} x${cell.count}" else cell.partName,
          style = MaterialTheme.typography.labelSmall,
          color = Color.DarkGray,
          fontSize = 10.sp,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}

/**
 * リポジトリから特定の装備、レリック階層（ Lith/Meso 等）に合致するアクティブな出現データを検索して返す
 */
private fun getCellData(
  repository: WarframeRepository,
  itemName: String, // 例: "Voruna Prime"
  tier: String,    // 例: "Lith"
  onLocalize: (String) -> String
): RelicTableCell? {
  
  // パーツ名の候補リスト
  // Warframeは: 設計図, シャーシ, システム, ニューロティックス
  // 武器は: 設計図, レシーバー, バレル, ストック, ブレード, ハンドル など
  val partSuffixes = listOf(
    "設計図", "シャーシ", "システム", "ニューロティックス",
    "レシーバー", "バレル", "ストック", "ブレード", "ハンドル", "グリップ", "ストリング", "肢", "リンク", "ポウチ"
  )

  // 1. 各パーツを逆引きする
  partSuffixes.forEach { suffix ->
    // パーツ名の一致検索キーを作る
    // 例: "Voruna Prime Chassis" にあたる uniqueName を特定したい
    // そのため、日本語辞書 (localizationDict) から逆引きする
    val localizedSearch = "${itemName} ${suffix}"
    
    // repository の localizationDict から、value が localizedSearch に該当する uniqueName を見つける
    val relicsForPart = repository.getRelicsForLocalizedItem(localizedSearch)
    
    relicsForPart.forEach { (relic, reward) ->
      // レリック名が該当の Tier (Lith等) かつアクティブであるか判定
      if (relic.name.startsWith(tier, ignoreCase = true) && repository.isRelicActive(relic.uniqueName)) {
        
        // レリック名から短い記号 (Lith N19 -> N19) を切り出す
        val shortName = relic.name.substringAfter(" ").replace(" レリック", "")
        
        // 数量バッジをレシピから取得
        // 例: Voruna Prime の設計図の中で、Chassis が何個必要か
        val parentUniqueName = repository.getAllRelics().flatMap { it.relicRewards ?: emptyList() }
          .find { onLocalize(it.rewardName) == itemName }?.rewardName ?: ""
        
        val count = repository.getIngredientCount(parentUniqueName, reward.rewardName)

        return RelicTableCell(
          relicNameShort = shortName,
          partName = suffix,
          count = count,
          rarity = reward.rarity
        )
      }
    }
  }

  return null
}
