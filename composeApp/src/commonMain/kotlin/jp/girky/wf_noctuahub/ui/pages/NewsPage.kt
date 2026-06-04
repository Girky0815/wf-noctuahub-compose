package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.girky.wf_noctuahub.data.api.model.WsEvent
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonGroup
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonOption
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.utils.currentTimeMillis
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * ゲーム内端末と同様に「ニュース」「パッチノート」「コミュニティ・配信」を
 * M3Eのボタングループで切り替えて閲覧できる公式インゲームニュース画面
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewsPage(
  events: List<WsEvent>?,
  onLocalize: (String) -> String,
  modifier: Modifier = Modifier
) {
  val uriHandler = LocalUriHandler.current
  var selectedTab by remember { mutableStateOf("news") } // "news", "patch", "community"

  // 1秒ごとに更新される現在時刻（配信のLIVE判定などに使用）
  var nowMs by remember { mutableStateOf(currentTimeMillis()) }
  LaunchedEffect(Unit) {
    while (true) {
      kotlinx.coroutines.delay(1000)
      nowMs = currentTimeMillis()
    }
  }

  // 安全にイベントリストを取得
  val safeEvents = events ?: emptyList()

  // 各要素の日本語タイトルを優先的に決定するヘルパー
  fun getEventTitle(event: WsEvent): String {
    val jaMsg = event.messages?.find { it.languageCode == "ja" }?.message
    return if (!jaMsg.isNullOrBlank()) {
      jaMsg
    } else {
      val raw = event.messages?.firstOrNull()?.message ?: "不明なお知らせ"
      onLocalize(raw)
    }
  }

  // カテゴリ分類ロジック
  val filteredEvents = remember(safeEvents, selectedTab) {
    safeEvents.filter { event ->
      val title = getEventTitle(event)
      val isPatch = title.contains("アップデート", ignoreCase = true) ||
                    title.contains("ホットフィックス", ignoreCase = true) ||
                    title.contains("修正", ignoreCase = true) ||
                    title.contains("Update", ignoreCase = true) ||
                    title.contains("Hotfix", ignoreCase = true) ||
                    title.contains("Patch", ignoreCase = true)
      
      val isCommunity = event.community == true || !event.eventLiveUrl.isNullOrBlank()

      when (selectedTab) {
        "patch" -> isPatch && !isCommunity // パッチノートカテゴリ
        "community" -> isCommunity // コミュニティ・配信カテゴリ
        else -> !isPatch && !isCommunity // 一般ニュースカテゴリ
      }
    }
  }

  Column(modifier = modifier.fillMaxSize()) {
    // ニュース画面のイントロダクションヘッダー
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
        text = "ニュース",
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "Origin 太陽系の最新アップデート情報、公式配信スケジュール、ゲーム内ニュースです。",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // M3E 準拠のボタングループによるタブ切り替え
    ExpressiveButtonGroup(
      options = listOf(
        ExpressiveButtonOption(label = "ニュース", icon = Icons.Rounded.Newspaper, onClick = { selectedTab = "news" }),
        ExpressiveButtonOption(label = "パッチノート", icon = Icons.Rounded.HistoryEdu, onClick = { selectedTab = "patch" }),
        ExpressiveButtonOption(label = "コミュニティ", icon = Icons.Rounded.People, onClick = { selectedTab = "community" })
      ),
      selectedIndex = when (selectedTab) {
        "news" -> 0
        "patch" -> 1
        "community" -> 2
        else -> 0
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    )

    // ニュースのコンテンツリスト
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (filteredEvents.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "現在このカテゴリのニュースはありません。",
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      } else {
        items(filteredEvents, key = { it.id?.oid ?: it.hashCode().toString() }) { event ->
          val title = getEventTitle(event)
          val dateLong = event.date?.epochMillis ?: 0L
          
          // 日付のJST変換と美フォーマット
          val dateStr = if (dateLong > 0L) {
            val dt = Instant.fromEpochMilliseconds(dateLong).toLocalDateTime(TimeZone.currentSystemDefault())
            "${dt.year}/${dt.monthNumber.toString().padStart(2, '0')}/${dt.dayOfMonth.toString().padStart(2, '0')}"
          } else {
            ""
          }

          // 配信スケジュールの解析とJSTフォーマット (コミュニティタブ用)
          val startMillis = event.eventStartDate?.epochMillis ?: 0L
          val endMillis = event.eventEndDate?.epochMillis ?: 0L
          val hasSchedule = startMillis > 0L && endMillis > 0L
          val isLive = hasSchedule && nowMs in startMillis..endMillis

          val scheduleStr = if (hasSchedule) {
            val startDt = Instant.fromEpochMilliseconds(startMillis).toLocalDateTime(TimeZone.currentSystemDefault())
            val endDt = Instant.fromEpochMilliseconds(endMillis).toLocalDateTime(TimeZone.currentSystemDefault())
            "${startDt.monthNumber}月${startDt.dayOfMonth}日 ${startDt.hour.toString().padStart(2, '0')}:${startDt.minute.toString().padStart(2, '0')} 〜 ${endDt.hour.toString().padStart(2, '0')}:${endDt.minute.toString().padStart(2, '0')} (JST)"
          } else {
            ""
          }

          // カスタムの美しいニュースリストカード
          Card(
            onClick = {
              event.prop?.let { uriHandler.openUri(it) }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
              contentColor = MaterialTheme.colorScheme.onSurface
            )
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // ヘッダー情報 (日付、カテゴリタグ、ライブバッジ)
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                // 日付
                if (dateStr.isNotEmpty()) {
                  Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                // ライブ配信バッジ
                if (isLive) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                      .clip(CircleShape)
                      .background(MaterialTheme.colorScheme.errorContainer)
                      .padding(horizontal = 10.dp, vertical = 2.dp)
                  ) {
                    Box(
                      modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                    )
                    Text(
                      text = "LIVE配信中",
                      color = MaterialTheme.colorScheme.onErrorContainer,
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }

              // ニュースタイトル
              Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )

              // 配信スケジュール
              if (scheduleStr.isNotEmpty()) {
                Card(
                  colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.padding(top = 4.dp)
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Rounded.AccessTime,
                      contentDescription = null,
                      modifier = Modifier.size(16.dp)
                    )
                    Text(
                      text = "配信時間: $scheduleStr",
                      style = MaterialTheme.typography.bodySmall,
                      fontWeight = FontWeight.Medium
                    )
                  }
                }
              }

              // アクションボタンとライブ先リンク
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Twitchなどの配信URLがある場合のボタン
                if (!event.eventLiveUrl.isNullOrBlank()) {
                  Button(
                    onClick = { uriHandler.openUri(event.eventLiveUrl) },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                      containerColor = MaterialTheme.colorScheme.primaryContainer,
                      contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Icon(
                        imageVector = Icons.Rounded.LiveTv,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                      )
                      Text(
                        text = "配信を観る",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                } else {
                  Spacer(modifier = Modifier.width(1.dp))
                }

                // 詳細リンク案内
                if (!event.prop.isNullOrBlank()) {
                  TextButton(
                    onClick = { uriHandler.openUri(event.prop) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                      Text(
                        text = "詳細を読む",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                      )
                      Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
