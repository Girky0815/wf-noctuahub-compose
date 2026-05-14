package jp.girky.wf_noctuahub.ui.components.fissures

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.model.FissureItem
import jp.girky.wf_noctuahub.utils.currentTimeMillis
import kotlinx.coroutines.delay

@Composable
fun FissureCard(fissure: FissureItem) {
    var now by remember { mutableStateOf(currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            now = currentTimeMillis()
        }
    }

    val subtitleText = if (fissure.enemyFaction.isNotBlank() && fissure.enemyFaction != "不明") {
        if (fissure.minLevel > 0) {
            "${fissure.missionType} | ${fissure.enemyFaction} (${fissure.minLevel} - ${fissure.maxLevel})"
        } else {
            "${fissure.missionType} | ${fissure.enemyFaction}"
        }
    } else {
        fissure.missionType
    }

    ListTile(
        title = fissure.node,
        subtitle = subtitleText,
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.HourglassBottom,
                    contentDescription = "残り時間",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatTimeRemaining(fissure.expiry, now),
                    style = jp.girky.wf_noctuahub.ui.theme.getAppTypographyCondensed().bodyMedium.copy(
                        fontFeatureSettings = "tnum"
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

private fun formatTimeRemaining(expiryMs: Long, nowMs: Long): String {
    val remaining = expiryMs - nowMs
    if (remaining <= 0) return "期限切れ"
    val totalSeconds = remaining / 1000
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "${m.toString().padStart(2, '0')}分${s.toString().padStart(2, '0')}秒"
}
