package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.ui.components.ui.EtaText
import jp.girky.wf_noctuahub.utils.Translations
import jp.girky.wf_noctuahub.utils.WikiUtils
import kotlinx.datetime.Instant

@Composable
fun ResurgencePage(
    worldState: WorldStateResponse?,
    onLocalize: (String) -> String
) {
    val uriHandler = LocalUriHandler.current
    val resurgence = worldState?.primeVaultTraders?.firstOrNull() ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Prime Resurgence 情報を取得できませんでした。")
        }
        return
    }

    val expiryLong = resurgence.expiry?.date?.numberLong?.toLongOrNull()
    val expiryString = expiryLong?.let { Instant.fromEpochMilliseconds(it).toString() }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = "Prime Resurgence",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
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
            Text(
                text = "過去の Prime が期間限定で復活。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )
        }

        if (primeWarframes.isNotEmpty()) {
            item {
                SectionTitle(title = "復活中の Prime Warframe", modifier = Modifier.padding(bottom = 8.dp))
                ListGroup {
                    for (resItem in primeWarframes) {
                        val name = onLocalize(resItem.itemType ?: "")
                        ListTile(
                            title = name,
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = "Wikiを開く",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            },
                            onClick = {
                                val url = WikiUtils.getResurgenceWarframeUrl(name)
                                uriHandler.openUri(url)
                            }
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
                            title = name,
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = "Wikiを開く",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            },
                            onClick = {
                                val url = WikiUtils.getResurgenceWeaponUrl(name)
                                uriHandler.openUri(url)
                            }
                        )
                    }
                }
            }
        }
    }
}
