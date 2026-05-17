package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.api.model.WsSortie
import jp.girky.wf_noctuahub.data.api.model.WsSortieVariant
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.utils.Translations

@Composable
fun SortiePage(
    worldState: WorldStateResponse?,
    onLocalize: (String) -> String,
    onGetRegionInfo: (String) -> jp.girky.wf_noctuahub.data.api.model.ExportRegion?
) {
    val sortie = worldState?.sorties?.firstOrNull() ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("ソーティー情報を取得できませんでした。")
        }
        return
    }

    val bossRaw = sortie.boss ?: "不明"
    val bossName = Translations.translateBoss(bossRaw)
    val faction = if (sortie.faction != null) {
        Translations.translateFaction(sortie.faction)
    } else {
        "不明"
    }
    val missions: List<WsSortieVariant> = sortie.variants ?: emptyList<WsSortieVariant>()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                text = "ソーティー",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "毎日更新される3段階のミッション。強力な報酬を獲得できます。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        item {
            SectionTitle(title = "$bossName ($faction)", modifier = Modifier.padding(bottom = 8.dp))
            ListGroup {
                for ((index, mission: WsSortieVariant) in missions.withIndex()) {
                    val typeTranslated = Translations.translateInternalMissionType(mission.missionType ?: "不明")
                    val nodeRaw = mission.node
                    val region = nodeRaw?.let { onGetRegionInfo(it) }
                    val nodeName = if (region != null) {
                        "${region.name} (${region.systemName})"
                    } else {
                        nodeRaw ?: "不明"
                    }
                    val sortieModifier = if (mission.modifierType != null) {
                        Translations.translateSortieModifier(mission.modifierType)
                    } else {
                        "なし"
                    }

                    ListTile(
                        title = nodeName,
                        subtitle = "$typeTranslated | $sortieModifier",
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
}
