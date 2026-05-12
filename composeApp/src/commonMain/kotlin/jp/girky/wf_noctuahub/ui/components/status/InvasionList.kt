package jp.girky.wf_noctuahub.ui.components.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.Reward
import jp.girky.wf_noctuahub.data.api.model.WsInvasion
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListItem
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.utils.Translations
import kotlin.math.abs

/**
 * 侵略ミッションのリストを表示するコンポーネント
 */
@Composable
fun InvasionList(
    invasions: List<WsInvasion>?,
    onLocalize: (String?) -> String,
    modifier: Modifier = Modifier
) {
    val activeInvasions = invasions?.filter { it.completed != true }

    if (activeInvasions.isNullOrEmpty()) {
        ListGroup(modifier = modifier) {
            ListTile(
                title = "侵略ミッションなし",
                subtitle = "現在発生している侵略はありません",
                onClick = null
            )
        }
        return
    }

    ListGroup(modifier = modifier) {
        activeInvasions.forEach { invasion ->
            val nodeName = Translations.translateNode(onLocalize(invasion.node))
            val locTagKey = invasion.locTag?.substringAfterLast("/") ?: ""
            val desc = Translations.translateInvasionDesc(locTagKey)
            
            val attackerFaction = Translations.translateFaction(invasion.faction.orEmpty().removePrefix("FC_"))
            val defenderFaction = Translations.translateFaction(invasion.defenderFaction.orEmpty().removePrefix("FC_"))
            
            val attackerRewardText = buildRewardText(invasion.attackerReward)
            val defenderRewardText = buildRewardText(invasion.defenderReward)

            // 進捗の計算 (公式APIのCountは 0 を基準に ±Goal へ向かう仕様と推定)
            val count = invasion.count ?: 0
            val goal = invasion.goal ?: 100000
            val safeGoal = if (goal == 0) 1 else abs(goal)
            
            // Attacker(攻撃側)の進行度: -goal ~ goal の範囲を 0.0 ~ 1.0 に正規化
            val progressFraction = ((count + safeGoal).toFloat() / (safeGoal * 2).toFloat()).coerceIn(0f, 1f)

            ListItem {
                // タイトル行 (ListTileの基本形を模倣)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = nodeName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 行内部にVSとプログレスバーを描画
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 攻撃側 (Attacker)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = attackerFaction, style = MaterialTheme.typography.labelSmall)
                            Text(
                                text = attackerRewardText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // VS
                        Text(
                            text = "VS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        // 防衛側 (Defender)
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text(text = defenderFaction, style = MaterialTheme.typography.labelSmall)
                            Text(
                                text = defenderRewardText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // プログレスバー
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest, shape = androidx.compose.foundation.shape.CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = progressFraction)
                                .height(8.dp)
                                .background(MaterialTheme.colorScheme.primary, shape = androidx.compose.foundation.shape.CircleShape)
                        )
                    }
                }
            }
        }
    }
}

private fun buildRewardText(reward: Reward?): String {
    if (reward == null) return "-"
    
    val parts = mutableListOf<String>()
    if (reward.credits > 0) {
        val creditsStr = formatWithCommas(reward.credits)
        parts.add("${creditsStr}Cr")
    }
    
    reward.countedItems?.forEach { item ->
        val itemName = item.itemType?.substringAfterLast("/") ?: "アイテム"
        parts.add("${Translations.translateInvasionReward(itemName)} x${item.itemCount}")
    }
    
    reward.items?.forEach { item ->
        val itemName = item.substringAfterLast("/")
        parts.add(Translations.translateInvasionReward(itemName))
    }
    
    return if (parts.isNotEmpty()) parts.joinToString(" + ") else "-"
}

private fun formatWithCommas(value: Int): String {
    return value.toString().reversed().chunked(3).joinToString(",").reversed()
}
