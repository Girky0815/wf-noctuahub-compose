package jp.girky.wf_noctuahub.ui.components.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Instant
import jp.girky.wf_noctuahub.utils.currentTimeMillis
import kotlin.time.Duration

/**
 * ISO-8601 形式の有効期限文字列 (expiryString) を受け取り、
 * 現在時刻からの残り時間 (ETA) を計算して 1秒に1回 表示を更新するコンポーネント。
 */
@Composable
fun EtaText(
    expiryString: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    if (expiryString.isNullOrBlank()) {
        Text(text = "不明", modifier = modifier, color = color)
        return
    }

    var remainingTimeText by remember { mutableStateOf(calculateEta(expiryString)) }

    LaunchedEffect(expiryString) {
        while (isActive) {
            remainingTimeText = calculateEta(expiryString)
            delay(1000L) // 1秒ごとに更新
        }
    }

    Text(
        text = remainingTimeText,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontFeatureSettings = "tnum"
        ),
        color = color
    )
}

/**
 * expiryString から 現在時刻を引いた残り時間の文字列フォーマットを返す
 */
private fun calculateEta(expiryString: String): String {
    try {
        val expiryInstant = Instant.parse(expiryString)
        val now = Instant.fromEpochMilliseconds(currentTimeMillis())
        val duration = expiryInstant - now

        if (duration.inWholeSeconds < 0) {
            return "終了"
        }

        return formatDuration(duration)
    } catch (e: Exception) {
        return "パースエラー"
    }
}

/**
 * 期間を "3d 12h 5m 30s" 形式（0の単位は省略）でフォーマットする
 */
private fun formatDuration(duration: Duration): String {
    val totalSeconds = duration.inWholeSeconds
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "${m.toString().padStart(2, '0')}分${s.toString().padStart(2, '0')}秒"
}
