package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.api.model.WsReward
import jp.girky.wf_noctuahub.ui.components.ui.ListItem
import jp.girky.wf_noctuahub.utils.currentTimeMillis
import jp.girky.wf_noctuahub.utils.Translations
import kotlinx.coroutines.delay

@Composable
fun EventsPage(
    worldState: WorldStateResponse?,
    onLocalize: (String) -> String
) {
    val goals = worldState?.goals ?: emptyList()

    // 1秒ごとに現在時刻を更新するState
    val nowState = produceState(initialValue = currentTimeMillis()) {
        while (true) {
            delay(1000)
            value = currentTimeMillis()
        }
    }
    val now = nowState.value

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "イベント",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "現在開催中のゲーム内イベント情報です。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (goals.isEmpty()) {
            item {
                Text(
                    text = "現在アクティブなイベントはありません。",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(goals) { eventGoal ->
                val eventDesc = if (eventGoal.desc != null) {
                    val eventNameTranslated = Translations.translateEvent(eventGoal.desc)
                    if (eventNameTranslated != eventGoal.desc) {
                        eventNameTranslated
                    } else {
                        onLocalize(eventGoal.desc)
                    }
                } else {
                    "不明なイベント"
                }

                val descLower = eventGoal.desc?.lowercase() ?: ""
                val tagLower = eventGoal.tag?.lowercase() ?: ""

                // 進捗を表示するもの
                val isProgressType = descLower.contains("jadeshadows") || 
                                     descLower.contains("belly") || 
                                     descLower.contains("ghoul") || 
                                     descLower.contains("heatfissures") || 
                                     tagLower.contains("jadeshadows") || 
                                     tagLower.contains("belly") || 
                                     tagLower.contains("ghoul") || 
                                     tagLower.contains("heatfissures")

                // 残り耐久力を表示するもの
                val isHealthType = descLower.contains("razorback") || 
                                   descLower.contains("fomorian") || 
                                   tagLower.contains("razorback") || 
                                   tagLower.contains("fomorian")

                val expiryLong = eventGoal.expiry?.epochMillis ?: 0L
                val diffMillis = expiryLong - now
                val timeString = if (expiryLong <= 0L) {
                    "時間情報なし"
                } else if (diffMillis <= 0L) {
                    "イベント終了"
                } else {
                    val totalSec = diffMillis / 1000
                    val sec = totalSec % 60
                    val min = (totalSec / 60) % 60
                    val hour = (totalSec / 3600) % 24
                    val day = totalSec / 86400
                    if (day > 0) {
                        "${day}日${hour}時間${min}分${sec}秒"
                    } else {
                        "${hour}時間${min}分${sec}秒"
                    }
                }

                ListItem(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(
                            text = eventDesc,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))

                        if (isProgressType) {
                            val count = eventGoal.count ?: 0
                            val rawGoal = eventGoal.goal
                            val progress = if (rawGoal != null && rawGoal > 0) {
                                count.toFloat() / rawGoal.toFloat()
                            } else {
                                count.toFloat() / 100f
                            }
                            val progressPercent = (progress * 100).toDouble()
                            val percentString = String.format("%.1f", progressPercent)

                            val progressText = if (rawGoal != null && rawGoal > 0) {
                                "進捗状況: $count / $rawGoal"
                            } else {
                                "進捗状況"
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = progressText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$percentString%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progress.coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(8.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primaryContainer,
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )

                            // 獣の巣窟作戦 (Jade Shadows) のための節目仕掛け
                            val isJadeShadows = descLower.contains("jadeshadows") || tagLower.contains("jadeshadows")
                            if (isJadeShadows) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("0%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    
                                    val activeColor = MaterialTheme.colorScheme.primary
                                    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    
                                    Text(
                                        text = "▼ 30% (第一報酬)", 
                                        style = MaterialTheme.typography.bodySmall, 
                                        fontWeight = if (progressPercent >= 30.0) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
                                        color = if (progressPercent >= 30.0) activeColor else inactiveColor
                                    )
                                    Text(
                                        text = "▼ 60% (第二報酬)", 
                                        style = MaterialTheme.typography.bodySmall, 
                                        fontWeight = if (progressPercent >= 60.0) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
                                        color = if (progressPercent >= 60.0) activeColor else inactiveColor
                                    )
                                    Text(
                                        text = "▼ 90% (第三報酬)", 
                                        style = MaterialTheme.typography.bodySmall, 
                                        fontWeight = if (progressPercent >= 90.0) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
                                        color = if (progressPercent >= 90.0) activeColor else inactiveColor
                                    )
                                    
                                    Text("100%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        } else if (isHealthType) {
                            val health = (eventGoal.healthPct ?: 1.0).toFloat()
                            val healthPercent = String.format("%.1f", health * 100)
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "残り耐久力",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$healthPercent%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { health.coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(8.dp),
                                color = MaterialTheme.colorScheme.error,
                                trackColor = MaterialTheme.colorScheme.errorContainer,
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                        } else {
                            val health = (eventGoal.healthPct ?: 1.0).toFloat()
                            val healthPercent = String.format("%.1f", health * 100)
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "進行度",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$healthPercent%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { health.coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(8.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.secondaryContainer,
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "残り時間: $timeString",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            val reward = eventGoal.reward
                            if (reward != null) {
                                val rewardText = reward.asString
                                if (rewardText != null && rewardText.isNotEmpty()) {
                                    Text(
                                        text = "報酬: ${onLocalize(rewardText)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(start = 8.dp)
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
