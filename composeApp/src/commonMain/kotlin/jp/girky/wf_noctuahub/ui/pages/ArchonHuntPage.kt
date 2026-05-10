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
    val archonHunts = worldState.liteSorties ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                text = "アルコン争奪戦",
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
                    text = "現在アクティブなアルコン争奪戦はありません",
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
    val bossName = hunt.boss?.let { onLocalize(it) } ?: "不明"
    val missions = hunt.variants ?: emptyList()

    Column {
        SectionTitle(title = bossName, modifier = Modifier.padding(bottom = 8.dp))
        ListGroup {
            missions.forEachIndexed { index, mission ->
                val missionType = mission.missionType?.let { onLocalize(it) } ?: "不明"
                val nodeName = mission.node?.let { onGetRegionInfo(it)?.name } ?: mission.node ?: ""
                val modifier = mission.modifierType?.let { onLocalize(it) }
                
                ListTile(
                    title = "ミッション ${index + 1}: $missionType",
                    subtitle = buildString {
                        append(nodeName)
                        if (modifier != null) {
                            append(" - $modifier")
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Adjust,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    }
}
