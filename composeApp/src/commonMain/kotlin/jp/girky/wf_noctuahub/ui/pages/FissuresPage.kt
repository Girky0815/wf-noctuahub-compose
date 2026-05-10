package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ModeStandby
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.ui.components.fissures.FissureCard
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.ui.model.FissureItem
import jp.girky.wf_noctuahub.utils.Translations
import jp.girky.wf_noctuahub.utils.currentTimeMillis
import kotlinx.coroutines.delay

import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonGroup
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonOption
import jp.girky.wf_noctuahub.data.api.model.ExportRegion

@Composable
fun FissuresPage(
    worldState: WorldStateResponse?,
    onLocalize: (String?) -> String,
    onGetRegionInfo: (String?) -> ExportRegion?
) {
    var filterMode by remember { mutableStateOf("normal") } // "normal", "hard", "storm"
    var tickSeconds by remember { mutableStateOf(currentTimeMillis() / 1000) }

    LaunchedEffect(Unit) {
        while(true) {
            delay(1000)
            tickSeconds = currentTimeMillis() / 1000
        }
    }

    val allFissures = remember(worldState) {
        mapFissures(worldState, onLocalize, onGetRegionInfo)
    }

    val filteredFissures = remember(allFissures, filterMode, tickSeconds) {
        val now = currentTimeMillis()
        allFissures.filter { fissure ->
            val modeMatches = when (filterMode) {
                "hard" -> fissure.isHard && !fissure.isStorm
                "storm" -> fissure.isStorm
                else -> !fissure.isHard && !fissure.isStorm
            }
            modeMatches && fissure.expiry > now
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Toggle Buttons (ButtonGroup)
        ExpressiveButtonGroup(
            options = listOf(
                ExpressiveButtonOption(label = "通常", onClick = { filterMode = "normal" }),
                ExpressiveButtonOption(label = "鋼の道のり", icon = Icons.Default.ModeStandby, onClick = { filterMode = "hard" }),
                ExpressiveButtonOption(label = "Void嵐", icon = Icons.Default.RocketLaunch, onClick = { filterMode = "storm" })
            ),
            selectedIndex = when (filterMode) {
                "normal" -> 0
                "hard" -> 1
                "storm" -> 2
                else -> 0
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        val groupedFissures = filteredFissures.groupBy { it.tier }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // グループ間の余白
        ) {
            groupedFissures.forEach { (tierName, missions) ->
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SectionTitle(
                            title = tierName,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        ListGroup {
                            missions.forEach { fissure ->
                                FissureCard(fissure)
                            }
                        }
                    }
                }

                if (tierName == "オムニア") {
                    item {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "亀裂ミッションの敵レベルについて",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "敵レベルは元のミッションのものを表示しています。亀裂ミッションでは敵レベルの数値は元のミッションと異なります（ゲーム内での独自処理により、ほとんどのミッションでは5～8程度高くなります）。",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun mapFissures(
    worldState: WorldStateResponse?,
    onLocalize: (String?) -> String,
    onGetRegionInfo: (String?) -> ExportRegion?
): List<FissureItem> {
    if (worldState == null) return emptyList()

    val normalMissions = worldState.activeMissions?.map { mission ->
        val tierStr = mission.modifier ?: ""
        val tierNum = parseTierNum(tierStr)
        val isHard = mission.hard ?: false
        val resolvedTier = tierNumToTierName(tierNum)

        val region = onGetRegionInfo(mission.node)
        val factionName = Translations.translateFactionIndex(region?.factionIndex)
        val minL = (region?.minEnemyLevel ?: 0).let { if (isHard) it + 100 else it }
        val maxL = (region?.maxEnemyLevel ?: 0).let { if (isHard) it + 100 else it }

        FissureItem(
            id = mission.id?.oid ?: "",
            node = Translations.translateNode(onLocalize(mission.node)),
            missionType = Translations.translateInternalMissionType(mission.missionType ?: "不明"),
            tier = resolvedTier,
            expiry = mission.expiry?.epochMillis ?: 0L,
            isHard = isHard,
            isStorm = false,
            enemyFaction = factionName,
            minLevel = minL,
            maxLevel = maxL,
            tierNum = tierNum
        )
    } ?: emptyList()

    val stormMissions = worldState.voidStorms?.map { storm ->
        val tierStr = storm.activeMissionTier ?: ""
        val tierNum = parseTierNum(tierStr)
        val resolvedTier = tierNumToTierName(tierNum)

        val region = onGetRegionInfo(storm.node)
        val factionName = Translations.translateFactionIndex(region?.factionIndex)
        val minL = region?.minEnemyLevel ?: 0
        val maxL = region?.maxEnemyLevel ?: 0

        FissureItem(
            id = storm.id?.oid ?: "",
            node = Translations.translateNode(onLocalize(storm.node)),
            missionType = "小戦", // Void嵐の場合はだいたいSkirmish(小戦)かVolatile等。今回は雑に固定か空文字
            tier = resolvedTier,
            expiry = storm.expiry?.epochMillis ?: 0L,
            isHard = false, 
            isStorm = true,
            enemyFaction = factionName,
            minLevel = minL,
            maxLevel = maxL,
            tierNum = tierNum
        )
    } ?: emptyList()

    return (normalMissions + stormMissions).sortedBy { it.tierNum }
}

private fun parseTierNum(modifier: String): Int {
    if (modifier.contains("T1")) return 1
    if (modifier.contains("T2")) return 2
    if (modifier.contains("T3")) return 3
    if (modifier.contains("T4")) return 4
    if (modifier.contains("T5")) return 5
    if (modifier.contains("T6")) return 6
    if (modifier.contains("Requiem", ignoreCase = true)) return 5
    if (modifier.contains("Omnia", ignoreCase = true)) return 6
    if (modifier.contains("Lith", ignoreCase = true)) return 1
    if (modifier.contains("Meso", ignoreCase = true)) return 2
    if (modifier.contains("Neo", ignoreCase = true)) return 3
    if (modifier.contains("Axi", ignoreCase = true)) return 4
    return 0
}

private fun tierNumToTierName(num: Int): String {
    return when(num) {
        1 -> "Lith"
        2 -> "Meso"
        3 -> "Neo"
        4 -> "Axi"
        5 -> "Requiem"
        6 -> "オムニア"
        else -> "不明"
    }
}
