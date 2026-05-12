package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.ui.components.status.AlertList
import jp.girky.wf_noctuahub.ui.components.status.CycleCard
import jp.girky.wf_noctuahub.ui.components.status.InvasionList
import jp.girky.wf_noctuahub.ui.components.ui.GridGroup
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.utils.LocalCycles
import jp.girky.wf_noctuahub.utils.currentTimeMillis

/**
 * WorldState情報を俯瞰するためのダッシュボード画面
 */
@Composable
fun StatusPage(
    worldState: WorldStateResponse?,
    onLocalize: (String?) -> String
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (worldState == null) {
            // ローディング中などの表示（App.kt 側で制御するため基本は到達しない前提）
            return@Column
        }

        val nowMs = currentTimeMillis()

        // サイクル計算
        val earthCycle = LocalCycles.getEarthCycle(nowMs)
        val cetusCycle = LocalCycles.getCetusCycle(nowMs)
        val vallisCycle = LocalCycles.getVallisCycle(nowMs)
        val cambionCycle = LocalCycles.getCambionCycle(nowMs)

        // ワールドサイクル (2列グリッド)
        GridGroup(columns = 2) {
            // 地球サイクル
            CycleCard(
                title = "地球: 森林タイル",
                stateText = if (earthCycle.isDay) "昼" else "夜",
                expiryString = earthCycle.expiry.toString(),
                stateColor = if (earthCycle.isDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
    
            // シータスサイクル
            CycleCard(
                title = "エイドロンの草原",
                stateText = if (cetusCycle.isDay) "昼" else "夜",
                expiryString = cetusCycle.expiry.toString(),
                stateColor = if (cetusCycle.isDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
    
            // オーブ峡谷サイクル
            CycleCard(
                title = "オーブ峡谷",
                stateText = if (vallisCycle.state == "warm") "温暖" else "寒冷",
                expiryString = vallisCycle.expiry.toString(),
                stateColor = if (vallisCycle.state == "warm") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
            )
    
            // カンビオン荒地サイクル
            CycleCard(
                title = "カンビオン荒地",
                stateText = if (cambionCycle.isDay) "Fass" else "Vome",
                expiryString = cambionCycle.expiry.toString(),
                stateColor = if (cambionCycle.isDay) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }

        // Zariman や Duviri はAPIの SyndicateMissions 等から抽出してここで表示予定 (一旦保留)

        // Zariman や Duviri はAPIの SyndicateMissions 等から抽出してここで表示予定 (一旦保留)

        // アラート
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionTitle("アラート")
            AlertList(alerts = worldState.alerts, onLocalize = onLocalize)
        }

        // 侵略
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionTitle("侵略")
            InvasionList(invasions = worldState.invasions, onLocalize = onLocalize)
        }
        
        Spacer(modifier = Modifier.height(32.dp)) // ボトムナビゲーション用の余白想定
    }
}
