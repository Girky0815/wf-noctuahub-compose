package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.ui.components.ui.EtaText
import jp.girky.wf_noctuahub.utils.Translations
import kotlinx.datetime.Instant

@Composable
fun NightwavePage(
    worldState: WorldStateResponse?,
    onTranslateNightwave: (String) -> Pair<String, String> = { Translations.translateNightwaveChallenge(it) }
) {
    val seasonInfo = worldState?.seasonInfo ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Nightwave 情報を取得できませんでした。")
        }
        return
    }

    val challenges = seasonInfo.activeChallenges ?: emptyList()
    val daily = challenges.filter { it.daily == true }
    
    // 通常のウィークリーとエリート・ウィークリーの分離
    val weeklyNormal = challenges.filter { 
        it.daily == false && !(it.challenge ?: "").contains("Hard") 
    }
    val weeklyElite = challenges.filter { 
        it.daily == false && (it.challenge ?: "").contains("Hard") 
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                text = "Nightwave",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "課題を完了して Nora Night から報酬を受け取りましょう。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        if (daily.isNotEmpty()) {
            item {
                SectionTitle(title = "デイリーアクト", modifier = Modifier.padding(bottom = 8.dp))
                ListGroup {
                    for (challenge in daily) {
                        val (titleText, descText) = onTranslateNightwave(challenge.challenge ?: "")
                        val expiryLong = challenge.expiry?.date?.numberLong?.toLongOrNull()
                        val expiryString = expiryLong?.let { Instant.fromEpochMilliseconds(it).toString() }
                        
                        ListTile(
                            title = titleText,
                            subtitle = if (descText.isNotEmpty()) descText else null,
                            trailingContent = {
                                if (expiryString != null) {
                                    EtaText(expiryString = expiryString)
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (weeklyNormal.isNotEmpty()) {
            val firstChallenge = weeklyNormal.first()
            val expiryLong = firstChallenge.expiry?.date?.numberLong?.toLongOrNull()
            val expiryString = expiryLong?.let { Instant.fromEpochMilliseconds(it).toString() }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    SectionTitle(title = "ウィークリーアクト")
                    if (expiryString != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                                EtaText(
                                    expiryString = expiryString,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
                ListGroup {
                    for (challenge in weeklyNormal) {
                        val (titleText, descText) = onTranslateNightwave(challenge.challenge ?: "")
                        
                        ListTile(
                            title = titleText,
                            subtitle = if (descText.isNotEmpty()) descText else null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (weeklyElite.isNotEmpty()) {
            val firstChallenge = weeklyElite.first()
            val expiryLong = firstChallenge.expiry?.date?.numberLong?.toLongOrNull()
            val expiryString = expiryLong?.let { Instant.fromEpochMilliseconds(it).toString() }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    SectionTitle(title = "エリート・ウィークリーアクト")
                    if (expiryString != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                                EtaText(
                                    expiryString = expiryString,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
                ListGroup {
                    for (challenge in weeklyElite) {
                        val (titleText, descText) = onTranslateNightwave(challenge.challenge ?: "")
                        
                        ListTile(
                            title = titleText,
                            subtitle = if (descText.isNotEmpty()) descText else null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
