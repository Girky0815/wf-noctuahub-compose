package jp.girky.wf_noctuahub.ui.components.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * React版の `ListGroup` に相当するコンポーネント。
 * 子要素（ListTileなど）をまとめるためのカード状のコンテナ。
 */
@Composable
fun ListGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp), // Tomato風の大きめの角丸
        colors = CardDefaults.cardColors(
            // ギャップ部分を透明にし、アプリの背景色を直接透けさせる
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
          // グループリストのギャップ
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp),
            content = content
        )
    }
}
