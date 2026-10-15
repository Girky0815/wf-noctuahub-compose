package jp.girky.wf_noctuahub.ui.components.prime

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.PrimeSeriesItem
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.utils.WikiUtils

/**
 * Primeシリーズ（各Primeパック）をセクション見出しと ListGroup / ListTile で表示するコンポーネント
 *
 * [レイアウト構成]
 * リストタイトル (例: SectionTitle("Frost Prime Pack"))
 *   ListGroup {
 *     ListTile (Prime Warframe - 例: Frost Prime, リンク&スター)
 *     ListTile (Prime 武器 - 例: Latron Prime, リンク&スター)
 *     ListTile (Prime 武器 - 例: Reaper Prime, リンク&スター)
 *   }
 *
 * @param series 表示対象のPrimeシリーズデータ
 * @param favoriteIds お気に入り登録されているIDのSet（uniqueNameまたはid）
 * @param onToggleFavorite お気に入り切り替えコールバック
 * @param modifier 修飾子
 */
@Composable
fun PrimeSeriesCard(
  series: PrimeSeriesItem,
  favoriteIds: Set<String>,
  onToggleFavorite: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val uriHandler = LocalUriHandler.current

  // シリーズタイトル（タイトルにはスターおよびリンクは不要）
  val seriesTitle = if (series.releaseDate.isNotBlank()) {
    "${series.name} (${series.releaseDate})"
  } else {
    series.name
  }

  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    // リストタイトル (xxx Prime)
    SectionTitle(title = seriesTitle)

    // リストアイテム群 (Prime Warframe、Prime 武器...)
    ListGroup {
      for (item in series.items) {
        val itemKey = item.uniqueName.ifBlank { "${series.id}_${item.name}" }
        val isFav = favoriteIds.contains(itemKey)
        val itemWikiUrl = WikiUtils.getPrimeItemUrl(item.name, item.category)

        ListTile(
          title = item.name,
          subtitle = item.category,
          trailingContent = {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              // リンクボタン
              IconButton(
                onClick = {
                  uriHandler.openUri(itemWikiUrl)
                }
              ) {
                Icon(
                  imageVector = Icons.Rounded.OpenInNew,
                  contentDescription = "${item.name} の Wiki を開く",
                  tint = MaterialTheme.colorScheme.primary
                )
              }

              // スターボタン (お気に入り)
              IconButton(
                onClick = {
                  onToggleFavorite(itemKey)
                }
              ) {
                Icon(
                  imageVector = if (isFav) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                  contentDescription = if (isFav) "${item.name} のお気に入りを解除" else "${item.name} をお気に入りに追加",
                  tint = if (isFav) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          },
          onClick = {
            // アイテム行タップで該当Wikiを開く
            uriHandler.openUri(itemWikiUrl)
          }
        )
      }
    }
  }
}
