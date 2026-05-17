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
import jp.girky.wf_noctuahub.utils.Translations

@Composable
fun ResurgencePage(
    worldState: WorldStateResponse?,
    onLocalize: (String) -> String
) {
    val resurgence = worldState?.primeVaultTraders?.firstOrNull() ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Prime Resurgence 情報を取得できませんでした。")
        }
        return
    }

    val manifest = resurgence.manifest ?: emptyList()
    
    // Powersuits を含むものを Prime Warframe として抽出
    val primeWarframes = manifest.filter { item ->
        val type = item.itemType ?: ""
        type.contains("Powersuits", ignoreCase = true)
    }

    // Weapons を含むものを Prime 武器 として抽出
    val primeWeapons = manifest.filter { item ->
        val type = item.itemType ?: ""
        type.contains("Weapons", ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                text = "Prime Resurgence",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "過去の Prime アイテムが期間限定で復活。アヤを使用して設計図を入手しましょう。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        if (primeWarframes.isNotEmpty()) {
            item {
                SectionTitle(title = "復活中の Prime Warframe", modifier = Modifier.padding(bottom = 8.dp))
                ListGroup {
                    for (resItem in primeWarframes) {
                        val name = onLocalize(resItem.itemType ?: "")
                        ListTile(
                            title = name
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (primeWeapons.isNotEmpty()) {
            item {
                SectionTitle(title = "復活中の Prime 武器", modifier = Modifier.padding(bottom = 8.dp))
                ListGroup {
                    for (resItem in primeWeapons) {
                        val name = onLocalize(resItem.itemType ?: "")
                        ListTile(
                            title = name
                        )
                    }
                }
            }
        }
    }
}
