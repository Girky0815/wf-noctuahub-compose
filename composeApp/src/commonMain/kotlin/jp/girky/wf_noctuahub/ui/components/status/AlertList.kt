package jp.girky.wf_noctuahub.ui.components.status

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WsAlert
import jp.girky.wf_noctuahub.ui.components.ui.EtaText
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.utils.Translations
import jp.girky.wf_noctuahub.utils.currentTimeMillis
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect

/**
 * 現在アクティブなアラートミッションのリストを表示するコンポーネント
 */
@Composable
fun AlertList(
    alerts: List<WsAlert>?,
    onLocalize: (String?) -> String,
    modifier: Modifier = Modifier
) {
    var now by remember { mutableStateOf(currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            now = currentTimeMillis()
        }
    }

    val activeAlerts = alerts?.filter { (it.expiry?.epochMillis ?: 0L) > now }

    if (activeAlerts.isNullOrEmpty()) {
        ListGroup(modifier = modifier) {
            ListTile(
                title = "アラートミッションなし",
                subtitle = "現在発生しているアラートはありません",
                onClick = null
            )
        }
        return
    }

    ListGroup(modifier = modifier) {
        activeAlerts.forEach { alert ->
            val mission = alert.missionInfo
            if (mission != null) {
                val locationName = onLocalize(mission.location)
                val missionType = Translations.translateInternalMissionType(mission.missionType.orEmpty())
                val faction = Translations.translateFaction(mission.faction.orEmpty().removePrefix("FC_"))
                val levelRange = "${mission.minEnemyLevel} - ${mission.maxEnemyLevel}"
                
                // 報酬テキストの構築
                val reward = mission.missionReward
                val rewardParts = mutableListOf<String>()
                if (reward?.credits != null && reward.credits > 0) {
                    val creditsStr = formatWithCommas(reward.credits)
                    rewardParts.add("${creditsStr}Cr")
                }
                reward?.countedItems?.forEach { item ->
                    // 内部パスから最後のアイテム名部分を抽出して翻訳
                    val itemName = item.itemType?.substringAfterLast("/") ?: "アイテム"
                    val translatedName = Translations.translateResource(itemName)
                    rewardParts.add("$translatedName x${item.itemCount}")
                }
                reward?.items?.forEach { item ->
                    val itemName = item.substringAfterLast("/")
                    val translatedName = Translations.translateResource(itemName)
                    rewardParts.add(translatedName)
                }
                val rewardText = if (rewardParts.isNotEmpty()) rewardParts.joinToString(" + ") else "報酬情報なし"

                val expiryMs = alert.expiry?.epochMillis ?: 0L

                ListTile(
                    title = locationName,
                    subtitle = "$missionType | $faction ($levelRange)\n報酬: $rewardText",
                    trailingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = "残り時間",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = formatTimeRemaining(expiryMs, now),
                                style = jp.girky.wf_noctuahub.ui.theme.getAppTypographyCondensed().bodyMedium.copy(
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        }
    }
}

private fun formatTimeRemaining(expiryMs: Long, nowMs: Long): String {
    val remaining = expiryMs - nowMs
    if (remaining <= 0) return "00分00秒"
    val totalSeconds = remaining / 1000
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "${m.toString().padStart(2, '0')}分${s.toString().padStart(2, '0')}秒"
}

private fun formatWithCommas(value: Int): String {
    return value.toString().reversed().chunked(3).joinToString(",").reversed()
}
