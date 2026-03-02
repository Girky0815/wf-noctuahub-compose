package jp.girky.wf_noctuahub.ui.components.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * ギャップを持つグリッドコンテナ。
 * 子要素を格子状に並べ、間隙には Card の背景色（`surfaceContainer`）を露出させます。
 */
@Composable
fun GridGroup(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    gap: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp), // Tomato風の大きめの角丸
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Layout(
            content = content,
            modifier = Modifier.fillMaxWidth()
        ) { measurables, constraints ->
            val gapPx = gap.roundToPx()
            val totalGapWidth = gapPx * (columns - 1)
            // 各カラムの等分された幅を計算
            val columnWidth = (constraints.maxWidth - totalGapWidth) / columns
            
            // 子要素を計算した幅で測定
            val itemConstraints = Constraints.fixedWidth(columnWidth)
            val placeables = measurables.map { it.measure(itemConstraints) }
            
            // 行数と、各行の最大高さを計算
            val rows = (placeables.size + columns - 1) / columns
            val rowHeights = IntArray(rows) { 0 }
            for (i in placeables.indices) {
                val r = i / columns
                rowHeights[r] = max(rowHeights[r], placeables[i].height)
            }
            
            // 全体の高さ
            val totalHeight = rowHeights.sum() + gapPx * (rows - 1)
            
            layout(constraints.maxWidth, totalHeight) {
                var y = 0
                for (r in 0 until rows) {
                    var x = 0
                    for (c in 0 until columns) {
                        val i = r * columns + c
                        if (i < placeables.size) {
                            placeables[i].placeRelative(x, y)
                            x += columnWidth + gapPx
                        }
                    }
                    y += rowHeights[r] + gapPx
                }
            }
        }
    }
}
