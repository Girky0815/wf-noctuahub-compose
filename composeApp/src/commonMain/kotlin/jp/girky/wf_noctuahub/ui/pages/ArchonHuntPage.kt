package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.api.model.WsSortie
import jp.girky.wf_noctuahub.data.api.model.WsSortieVariant
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning

@Composable
fun ArchonHuntPage(
    worldState: WorldStateResponse?,
    onLocalize: (String) -> String,
    onGetRegionInfo: (String) -> jp.girky.wf_noctuahub.data.api.model.ExportRegion?
) {
    if (worldState == null) {
        jp.girky.wf_noctuahub.ui.components.ui.CenteredLoadingIndicator()
        return
    }

    // Archon Hunt は API上では LiteSorties として提供される
    val archonHunts = worldState.liteSorties?.filter { 
        it.reward?.contains("Archon") == true || 
        it.boss?.contains("NIRA", ignoreCase = true) == true || 
        it.boss?.contains("AMAR", ignoreCase = true) == true || 
        it.boss?.contains("BOREAL", ignoreCase = true) == true 
    } ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                text = "アルコン討伐戦",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "毎週更新される高難易度ミッション。アルコンの欠片を入手できます。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        if (archonHunts.isEmpty()) {
            item {
                Text(
                    text = "現在アクティブなアルコン討伐戦はありません",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            items(archonHunts) { hunt ->
                ArchonHuntCard(hunt = hunt, onLocalize = onLocalize, onGetRegionInfo = onGetRegionInfo)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ArchonHuntCard(
    hunt: WsSortie,
    onLocalize: (String) -> String,
    onGetRegionInfo: (String) -> jp.girky.wf_noctuahub.data.api.model.ExportRegion?
) {
    val bossRaw = hunt.boss ?: "不明"
    val bossName = jp.girky.wf_noctuahub.utils.Translations.translateBoss(bossRaw)
    val missions = hunt.missions ?: emptyList()

    Column {
        SectionTitle(title = bossName, modifier = Modifier.padding(bottom = 8.dp))
        ListGroup {
            missions.forEachIndexed { index, mission ->
                val typeTranslated = jp.girky.wf_noctuahub.utils.Translations.translateInternalMissionType(mission.missionType ?: "不明")
                val nodeRaw = mission.node
                val region = nodeRaw?.let { onGetRegionInfo(it) }
                val nodeName = region?.let { "${it.name} (${it.systemName})" } ?: nodeRaw ?: "不明"
                
                // アルコン討伐戦は勢力とレベルが固定
                val factionTranslated = "ナルメル"
                val levelRange = when (index) {
                    0 -> "130 - 135"
                    1 -> "135 - 140"
                    2 -> "145 - 150"
                    else -> "130 - 150"
                }
                
                val subtitleText = "$typeTranslated | $factionTranslated ($levelRange)"
                
                ListTile(
                    title = nodeName,
                    subtitle = subtitleText,
                    leadingIcon = {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
        }
    }
}
