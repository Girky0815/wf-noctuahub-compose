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
import kotlinx.datetime.Instant

/**
 * 現在アクティブなアラートミッションのリストを表示するコンポーネント
 */
@Composable
fun AlertList(
    alerts: List<WsAlert>?,
    onLocalize: (String?) -> String,
    modifier: Modifier = Modifier
) {
    if (alerts.isNullOrEmpty()) {
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
        alerts.forEach { alert ->
            val mission = alert.missionInfo
            if (mission != null) {
                val locationName = onLocalize(mission.location)
                val missionType = Translations.translateMissionType(mission.missionType.orEmpty())
                val faction = Translations.translateFaction(mission.faction.orEmpty())
                val levelRange = "${mission.minEnemyLevel} - ${mission.maxEnemyLevel}"
                
                // 報酬テキストの構築
                val reward = mission.missionReward
                val rewardParts = mutableListOf<String>()
                if (reward?.credits != null && reward.credits > 0) {
                    rewardParts.add("${reward.credits}cr")
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

                val expiryString = alert.expiry?.epochMillis?.let { Instant.fromEpochMilliseconds(it).toString() }

                ListTile(
                    title = "$missionType ($locationName)",
                    subtitle = "$faction Lv$levelRange\n報酬: $rewardText",
                    trailingContent = {
                        Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                            Text(
                                text = "残り時間",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            EtaText(expiryString = expiryString)
                        }
                    }
                )
            }
        }
    }
}
