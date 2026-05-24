package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.repository.WarframeRepository
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonGroup
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonOption

/**
 * メニューの「レリック」を選択した際の統合親ページ。
 * 「レリックテーブル」と「レリック・シミュレーター」をタブ（ExpressiveButtonGroup）で切り替えます。
 */
@Composable
fun RelicsPage(
  repository: WarframeRepository,
  worldState: WorldStateResponse?,
  onLocalize: (String) -> String
) {
  var selectedTab by remember { mutableStateOf(0) }

  Column(
    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
  ) {
    ExpressiveButtonGroup(
      options = listOf(
        ExpressiveButtonOption(label = "レリックテーブル", onClick = { selectedTab = 0 }),
        ExpressiveButtonOption(label = "レリック・シミュレーター", onClick = { selectedTab = 1 })
      ),
      selectedIndex = selectedTab,
      modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    )

    Box(modifier = Modifier.fillMaxSize()) {
      when (selectedTab) {
        0 -> RelicTablePage(
          repository = repository,
          worldState = worldState,
          onLocalize = onLocalize
        )
        1 -> RelicSimulatorPage(
          repository = repository,
          onLocalize = onLocalize
        )
      }
    }
  }
}
