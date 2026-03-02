package jp.girky.wf_noctuahub.ui.components.status

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.ui.components.ui.EtaText
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle

/**
 * 地球、シータス、カンビオン荒地などの
 * 昼夜・状態サイクルを表示する共通カードコンポーネント
 */
@Composable
fun CycleCard(
    title: String,
    stateText: String,
    expiryString: String?,
    modifier: Modifier = Modifier,
    stateColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    ListGroup(modifier = modifier) {
        ListTile(
            title = title,
            subtitle = null,
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stateText,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = stateColor,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    EtaText(expiryString = expiryString)
                }
            }
        )
    }
}
