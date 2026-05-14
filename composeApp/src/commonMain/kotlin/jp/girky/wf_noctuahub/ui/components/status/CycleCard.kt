package jp.girky.wf_noctuahub.ui.components.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.utils.CycleState
import jp.girky.wf_noctuahub.utils.currentTimeMillis
import kotlinx.coroutines.delay

/**
 * 地球、シータス、カンビオン荒地などの
 * 昼夜・状態サイクルを自動更新して表示する共通カードコンポーネント
 */
@Composable
fun CycleCard(
    title: String,
    cycleGenerator: (Long) -> CycleState,
    stateTextFormatter: @Composable (CycleState) -> String,
    stateColorFormatter: @Composable (CycleState) -> androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    var nowMs by remember { mutableStateOf(currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            nowMs = currentTimeMillis()
        }
    }

    val currentCycle = cycleGenerator(nowMs)
    val stateText = stateTextFormatter(currentCycle)
    val stateColor = stateColorFormatter(currentCycle)
    
    val remainingMs = currentCycle.expiry.toEpochMilliseconds() - nowMs
    val remainingText = formatCycleTimeRemaining(remainingMs)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceBright, shape = RoundedCornerShape(2.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stateText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = stateColor
            )
            Text(
                text = remainingText,
                style = jp.girky.wf_noctuahub.ui.theme.getAppTypographyCondensed().bodyMedium.copy(
                    fontFeatureSettings = "tnum"
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatCycleTimeRemaining(remainingMs: Long): String {
    if (remainingMs <= 0) return "更新中..."
    val totalSeconds = remainingMs / 1000
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "${m.toString().padStart(2, '0')}分${s.toString().padStart(2, '0')}秒"
}
