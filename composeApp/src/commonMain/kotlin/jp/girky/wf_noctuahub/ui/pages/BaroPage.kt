package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import jp.girky.wf_noctuahub.utils.currentTimeMillis
import kotlinx.datetime.Instant

@Composable
fun BaroPage(
    worldState: WorldStateResponse?,
    onLocalize: (String) -> String
) {
    val baro = worldState?.voidTraders?.firstOrNull() ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Baro Ki'Teer 情報を取得できませんでした。")
        }
        return
    }

    val now = currentTimeMillis()
    val activationLong = baro.activation?.date?.numberLong?.toLongOrNull()
    val expiryLong = baro.expiry?.date?.numberLong?.toLongOrNull()
    val isActive = activationLong != null && expiryLong != null && now >= activationLong && now < expiryLong

    val location = baro.node?.let { onLocalize(it) } ?: "不明"
    val activationString = activationLong?.let { Instant.fromEpochMilliseconds(it).toString() }
    val expiryString = expiryLong?.let { Instant.fromEpochMilliseconds(it).toString() }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                text = "Baro Ki'Teer",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (isActive) {
                Text(
                    text = "Voidの商人が到着しました！リレーで珍しいアイテムを販売しています。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            } else {
                Text(
                    text = "Baro Ki'Teer は現在移動中です。次の到着をお楽しみに。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }

        item {
            SectionTitle(title = "ステータス", modifier = Modifier.padding(bottom = 8.dp))
            ListGroup {
                ListTile(
                    title = "場所",
                    subtitle = location
                )
                ListTile(
                    title = if (isActive) "離脱まで" else "到着まで",
                    trailingContent = {
                        val targetString = if (isActive) expiryString else activationString
                        if (targetString != null) {
                            EtaText(expiryString = targetString)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (isActive && baro.manifest != null && baro.manifest.isNotEmpty()) {
            item {
                SectionTitle(title = "販売品リスト", modifier = Modifier.padding(bottom = 8.dp))
                ListGroup {
                    for (baroItem in baro.manifest) {
                        val name = onLocalize(baroItem.itemType ?: "")
                        val ducats = baroItem.primePrice ?: 0
                        val credits = baroItem.regularPrice ?: 0
                        
                        ListTile(
                            title = name,
                            subtitle = "${ducats} Ducats | ${credits} Credits"
                        )
                    }
                }
            }
        }
    }
}
