package jp.girky.wf_noctuahub.ui.components.prime

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.ui.components.ui.CenteredLoadingIndicator
import jp.girky.wf_noctuahub.ui.viewmodel.PrimeSeriesViewModel

/**
 * Primeシリーズ一覧画面コンポーネント
 *
 * 検索バー、お気に入り切り替え、Wiki連携を備えたカードリストを表示する。
 *
 * @param viewModel PrimeSeriesViewModel
 * @param modifier 修飾子
 * @param showHeader タイトルヘッダーを表示するかどうか（サブページなどで別ヘッダーがある場合はfalse）
 */
@Composable
fun PrimeSeriesList(
  viewModel: PrimeSeriesViewModel,
  modifier: Modifier = Modifier,
  showHeader: Boolean = true
) {
  val uiState by viewModel.uiState.collectAsState()

  Column(
    modifier = modifier.fillMaxWidth()
  ) {
    if (showHeader) {
      // 画面タイトル
      Text(
        text = "Prime シリーズ一覧",
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
      )
      Text(
        text = "欲しい Prime のお気に入り登録や Warframe Wiki へのアクセスが可能です。",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 16.dp)
      )
    }

    // 検索バー
    OutlinedTextField(
      value = uiState.searchQuery,
      onValueChange = { viewModel.onSearchQueryChanged(it) },
      modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
      placeholder = { Text("シリーズ名またはアイテム名で検索") },
      leadingIcon = {
        Icon(
          imageVector = Icons.Rounded.Search,
          contentDescription = "検索アイコン",
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      },
      trailingIcon = {
        if (uiState.searchQuery.isNotEmpty()) {
          IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
            Icon(
              imageVector = Icons.Rounded.Clear,
              contentDescription = "検索文字をクリア",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      },
      shape = RoundedCornerShape(12.dp),
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright
      )
    )

    // ローディング中表示
    if (uiState.isLoading && uiState.seriesList.isEmpty()) {
      CenteredLoadingIndicator()
      return
    }

    // エラー時の表示
    if (uiState.errorMessage != null && uiState.filteredSeriesList.isEmpty()) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = uiState.errorMessage ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
          )
          Button(onClick = { viewModel.loadData() }) {
            Text("再試行")
          }
        }
      }
      return
    }

    // 該当なし表示
    if (uiState.filteredSeriesList.isEmpty()) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = if (uiState.searchQuery.isNotEmpty()) "一致する Prime シリーズが見つかりませんでした" else "Prime シリーズデータがありません",
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      return
    }

    // カード一覧
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      for (series in uiState.filteredSeriesList) {
        key(series.id) {
          PrimeSeriesCard(
            series = series,
            favoriteIds = uiState.favoriteIds,
            onToggleFavorite = { id -> viewModel.toggleFavorite(id) }
          )
        }
      }
    }
  }
}
