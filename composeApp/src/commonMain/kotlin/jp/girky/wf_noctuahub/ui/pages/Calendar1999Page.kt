package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.api.model.WsCalendarDay
import jp.girky.wf_noctuahub.data.api.model.WsCalendarEvent
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.utils.Translations

@Composable
fun Calendar1999Page(
  worldState: WorldStateResponse?,
  onLocalize: (String) -> String,
  showRawPaths: Boolean = false
) {
  val season = worldState?.calendarSeasons?.firstOrNull() ?: run {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
      Text("1999 Calendar 情報を取得できませんでした。")
    }
    return
  }

  val seasonName = Translations.translateCalendarSeason(season.season ?: "不明")
  val days: List<WsCalendarDay> = season.days ?: emptyList<WsCalendarDay>()

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(16.dp)
  ) {
    item {
      Text(
        text = "1999 Calendar",
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
      )
      Text(
        text = "現在の季節: $seasonName",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 24.dp)
      )
    }

    for (day in days) {
      item {
        val events: List<WsCalendarEvent> = day.events ?: emptyList<WsCalendarEvent>()
        
        Row(
          modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
          verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          SectionTitle(title = formatDayOfYear(day.day))
          
          if (events.isEmpty()) {
            Surface(
              shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.secondaryContainer,
              contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
              Text(
                text = "誕生日",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          } else {
            val eventTypes = events.mapNotNull { it.type }.distinct()
            for (type in eventTypes) {
              val label = Translations.translateCalendarType(type)
              val containerColor = when (type) {
                "CET_CHALLENGE" -> MaterialTheme.colorScheme.primaryContainer
                "CET_REWARD" -> MaterialTheme.colorScheme.tertiaryContainer
                "CET_UPGRADE" -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
              }
              val contentColor = when (type) {
                "CET_CHALLENGE" -> MaterialTheme.colorScheme.onPrimaryContainer
                "CET_REWARD" -> MaterialTheme.colorScheme.onTertiaryContainer
                "CET_UPGRADE" -> MaterialTheme.colorScheme.onSecondaryContainer
                else -> MaterialTheme.colorScheme.onSurfaceVariant
              }
              Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                color = containerColor,
                contentColor = contentColor
              ) {
                Text(
                  text = label,
                  style = MaterialTheme.typography.labelSmall,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }
        }

        ListGroup {
          if (events.isEmpty()) {
            val birthdayMap = mapOf(
              1 to "Kaya の誕生日",
              45 to "Lettie の誕生日",
              74 to "Minerva の誕生日",
              143 to "Amir の誕生日",
              166 to "Flare の誕生日",
              191 to "Aoi の誕生日",
              306 to "Eleanor の誕生日",
              307 to "Arthur の誕生日",
              338 to "Quincy の誕生日",
              355 to "Velimir の誕生日"
            )
            val birthdayName = birthdayMap[day.day] ?: "Protoframe の誕生日"
            ListTile(
              title = birthdayName,
              subtitle = if (showRawPaths) "Day ${day.day}" else null,
              titleColor = MaterialTheme.colorScheme.secondary
            )
          } else {
            for (calEvent in events) {
              val typeRaw = calEvent.type ?: "不明"
              val rawPath = calEvent.challenge ?: calEvent.reward ?: calEvent.upgrade ?: ""
              
              val (title, description) = if (rawPath.isNotBlank()) {
                val translation = Translations.translateCalendarEvent(rawPath)
                if (translation.first == rawPath.substringAfterLast("/")) {
                  // マップに翻訳がない場合は汎用 localize を使い説明は空にする
                  Pair(onLocalize(rawPath), "")
                } else {
                  translation
                }
              } else {
                Pair("不明", "")
              }
              
              val color = when (typeRaw) {
                "CET_CHALLENGE" -> MaterialTheme.colorScheme.primary
                "CET_REWARD" -> MaterialTheme.colorScheme.tertiary
                "CET_UPGRADE" -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.onSurface
              }

              val subtitleText = when {
                showRawPaths && description.isNotBlank() -> "$description\n($rawPath)"
                showRawPaths && description.isBlank() -> "($rawPath)"
                !showRawPaths && description.isNotBlank() -> description
                else -> null
              }

              ListTile(
                title = title,
                subtitle = subtitleText,
                titleColor = color
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

/**
 * 1月1日からの通算日（Day xxx）を平年（1999年）のカレンダーに基づいて「月日 (Day xxx)」形式に変換する
 */
private fun formatDayOfYear(dayOfYear: Int?): String {
  if (dayOfYear == null) return "不明"
  
  val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
  var remainingDays = dayOfYear
  
  if (remainingDays < 1 || remainingDays > 365) {
    return "Day $dayOfYear"
  }

  for (monthIndex in 0..11) {
    val days = daysInMonth[monthIndex]
    if (remainingDays <= days) {
      val month = monthIndex + 1
      return "${month}月${remainingDays}日 (Day $dayOfYear)"
    }
    remainingDays -= days
  }
  return "Day $dayOfYear"
}
