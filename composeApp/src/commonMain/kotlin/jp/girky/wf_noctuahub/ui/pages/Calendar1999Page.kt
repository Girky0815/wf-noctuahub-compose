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
    onLocalize: (String) -> String
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
                SectionTitle(title = formatDayOfYear(day.day), modifier = Modifier.padding(bottom = 8.dp))
                ListGroup {
                    val events: List<WsCalendarEvent> = day.events ?: emptyList<WsCalendarEvent>()
                    for (calEvent in events) {
                        val typeRaw = calEvent.type ?: "不明"
                        val typeName = Translations.translateCalendarType(typeRaw)
                        val content = if (calEvent.challenge != null) {
                            onLocalize(calEvent.challenge)
                        } else if (calEvent.reward != null) {
                            onLocalize(calEvent.reward)
                        } else if (calEvent.upgrade != null) {
                            onLocalize(calEvent.upgrade)
                        } else {
                            "不明"
                        }
                        
                        val color = when (typeRaw) {
                            "CET_CHALLENGE" -> MaterialTheme.colorScheme.primary
                            "CET_REWARD" -> MaterialTheme.colorScheme.tertiary
                            "CET_UPGRADE" -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.onSurface
                        }

                        ListTile(
                            title = content,
                            subtitle = typeName,
                            titleColor = color
                        )
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
