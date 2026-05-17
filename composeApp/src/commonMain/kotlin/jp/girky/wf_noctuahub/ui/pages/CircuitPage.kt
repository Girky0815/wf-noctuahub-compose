package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.api.model.WsEndlessXpChoice
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.utils.Translations

@Composable
fun CircuitPage(
    worldState: WorldStateResponse?,
    onLocalize: (String) -> String
) {
    val circuitRewards: List<WsEndlessXpChoice> = worldState?.endlessXpChoices ?: emptyList<WsEndlessXpChoice>()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                text = "サーキット",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "デュヴィリのサーキットで獲得可能な報酬リストです。毎週更新されます。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        if (circuitRewards.isEmpty()) {
            item {
                Text("現在サーキット報酬の情報がありません。")
            }
        } else {
            for (circuitReward in circuitRewards) {
                item {
                    val categoryName = if (circuitReward.circuitCategory != null) {
                        Translations.translateCircuitCategory(circuitReward.circuitCategory)
                    } else {
                        "不明"
                    }
                    SectionTitle(title = categoryName, modifier = Modifier.padding(bottom = 8.dp))
                    ListGroup {
                        val rewardItems: List<String> = if (circuitReward.choices != null) circuitReward.choices!! else listOf<String>()
                        for (itemName in rewardItems) {
                            val translated = onLocalize(itemName)
                            ListTile(title = translated)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
