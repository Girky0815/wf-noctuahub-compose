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
import jp.girky.wf_noctuahub.data.api.model.WsConquest
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.utils.Translations

@Composable
fun ArchimedeaPage(
    worldState: WorldStateResponse?,
    onLocalize: (String) -> String
) {
    if (worldState == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val now = (worldState.time ?: 0L) * 1000L
    val activeConquests = worldState.conquests?.filter {
        val start = it.activation?.epochMillis ?: 0L
        val end = it.expiry?.epochMillis ?: Long.MAX_VALUE
        it.type == "CT_LAB" && now in start..end
    } ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                text = "深淵アルキメデア",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Albrechtの研究所で高難易度のミッションを連続してクリアするモード。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        if (activeConquests.isEmpty()) {
            item {
                Text(
                    text = "現在アクティブな深淵アルキメデアはありません",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            items(activeConquests) { conquest ->
                ArchimedeaCard(conquest = conquest)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ArchimedeaCard(
    conquest: WsConquest
) {
    val missions = conquest.missions ?: emptyList()
    val variables = conquest.variables ?: emptyList()

    Column {
        SectionTitle(title = "ミッション", modifier = Modifier.padding(bottom = 8.dp))
        ListGroup {
            missions.forEachIndexed { index, mission ->
                val typeRaw = mission.missionType ?: "不明"
                val typeTranslated = Translations.translateInternalMissionType(typeRaw)
                
                val hardDifficulty = mission.difficulties?.find { it.type == "CD_HARD" }
                val normalDifficulty = mission.difficulties?.find { it.type == "CD_NORMAL" }
                val diff = hardDifficulty ?: normalDifficulty
                
                val subtitleText = buildString {
                    diff?.deviation?.let { append("偏差: ${formatModifierName(it)}\n") }
                    diff?.risks?.takeIf { it.isNotEmpty() }?.let { risks ->
                        append("リスク: ${risks.joinToString(", ") { formatModifierName(it) }}")
                    }
                }

                ListTile(
                    title = typeTranslated,
                    subtitle = subtitleText.trim(),
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

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle(title = "パーソナルモディファイア", modifier = Modifier.padding(bottom = 8.dp))
        ListGroup {
            variables.forEach { variable ->
                ListTile(
                    title = formatModifierName(variable),
                    onClick = null
                )
            }
        }
    }
}

private fun formatModifierName(raw: String): String {
    // CamelCase をスペース区切りにして Translations を通す
    val spaced = raw.replace(Regex("([a-z])([A-Z]+)"), "$1 $2")
    
    // 特定のキーの直接マッピング
    val directMap = mapOf(
        "VolatileGrenades" to "危険物資",
        "StickyFingers" to "危険エリア",
        "Reinforcements" to "戦力配備",
        "RegeneratingEnemies" to "敵性再生",
        "TimeDilation" to "アビリティ短縮",
        "VoidEnergyOverload" to "アビリティオーバーロード",
        "AbilityLockout" to "アビリティ封印",
        "Exhaustion" to "エネルギー枯渇",
        "ShieldedFoes" to "封鎖装甲",
        "Deflectors" to "適応抵抗",
        "Quicksand" to "絡み合い",
        "EMPBlackHole" to "磁気", // Guess
        "ExplosiveCrawlers" to "爆発的可能性" // Guess
    )
    
    directMap[raw]?.let { return it }
    
    val translated = Translations.translateArchimedeaModifierName(spaced, "C T_ L A B")
    return if (translated != spaced) translated else raw
}
