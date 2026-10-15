package jp.girky.wf_noctuahub.ui.viewmodel

import jp.girky.wf_noctuahub.data.api.model.PrimeSeriesData
import jp.girky.wf_noctuahub.data.api.model.PrimeSeriesItem
import jp.girky.wf_noctuahub.data.repository.AppSettings
import jp.girky.wf_noctuahub.data.repository.PrimeSeriesRepository
import jp.girky.wf_noctuahub.data.repository.PrimeSeriesState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Primeシリーズ一覧画面向けのUI状態
 *
 * @property isLoading 読み込み中フラグ
 * @property seriesList 取得されたPrimeSeriesItemのリスト
 * @property favoriteIds お気に入り登録されているIDのSet（シリーズIDまたはアイテムuniqueName）
 * @property searchQuery 検索クエリ文字列
 * @property filteredSeriesList 検索クエリによって絞り込まれたPrimeSeriesItemのリスト
 * @property errorMessage エラー発生時のメッセージ
 */
data class PrimeSeriesUiState(
  val isLoading: Boolean = false,
  val seriesList: List<PrimeSeriesItem> = emptyList(),
  val favoriteIds: Set<String> = emptySet(),
  val searchQuery: String = "",
  val filteredSeriesList: List<PrimeSeriesItem> = emptyList(),
  val errorMessage: String? = null
)

/**
 * Primeシリーズデータとお気に入り状態を統合管理するViewModel
 *
 * @property repository PrimeSeriesRepository
 * @property appSettings AppSettings
 * @property scope コルーチンスコープ
 */
class PrimeSeriesViewModel(
  private val repository: PrimeSeriesRepository,
  private val appSettings: AppSettings,
  private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {

  // 検索クエリ状態
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery

  // PrimeSeriesRepository のロード状態と AppSettings のお気に入り、検索クエリを合成したUI状態
  val uiState: StateFlow<PrimeSeriesUiState> = combine(
    repository.state,
    appSettings.favoritePrimeIdsFlow,
    _searchQuery
  ) { repoState, favoriteIds, query ->
    when (repoState) {
      is PrimeSeriesState.Idle -> PrimeSeriesUiState()
      is PrimeSeriesState.Loading -> PrimeSeriesUiState(
        isLoading = true,
        favoriteIds = favoriteIds,
        searchQuery = query
      )
      is PrimeSeriesState.Success -> {
        val series = repoState.data.series
        val filtered = filterSeries(series, query)
        PrimeSeriesUiState(
          isLoading = false,
          seriesList = series,
          favoriteIds = favoriteIds,
          searchQuery = query,
          filteredSeriesList = filtered
        )
      }
      is PrimeSeriesState.Error -> {
        val cachedSeries = repoState.cachedData?.series ?: emptyList()
        val filtered = filterSeries(cachedSeries, query)
        PrimeSeriesUiState(
          isLoading = false,
          seriesList = cachedSeries,
          favoriteIds = favoriteIds,
          searchQuery = query,
          filteredSeriesList = filtered,
          errorMessage = repoState.error.message ?: "データの読み込みに失敗しました"
        )
      }
    }
  }.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = PrimeSeriesUiState()
  )

  init {
    loadData()
  }

  /**
   * データを読み込む
   */
  fun loadData() {
    scope.launch {
      try {
        repository.getPrimeSeriesData()
      } catch (e: Exception) {
        // エラーハンドリングはリポジトリの内部状態(Error)で通知される
      }
    }
  }

  /**
   * 検索クエリを更新する
   */
  fun onSearchQueryChanged(newQuery: String) {
    _searchQuery.value = newQuery
  }

  /**
   * お気に入りIDの登録・解除をトグルする
   *
   * @param id シリーズIDまたはアイテムのuniqueName
   */
  fun toggleFavorite(id: String) {
    appSettings.toggleFavoritePrimeId(id)
  }

  /**
   * 指定したIDがお気に入り登録されているかどうか判定する
   */
  fun isFavorite(id: String): Boolean {
    return appSettings.getFavoritePrimeIds().contains(id)
  }

  private fun filterSeries(seriesList: List<PrimeSeriesItem>, query: String): List<PrimeSeriesItem> {
    val trimmed = query.trim()
    if (trimmed.isEmpty()) return seriesList
    return seriesList.filter { series ->
      series.name.contains(trimmed, ignoreCase = true) ||
        series.items.any { item -> item.name.contains(trimmed, ignoreCase = true) }
    }
  }
}
