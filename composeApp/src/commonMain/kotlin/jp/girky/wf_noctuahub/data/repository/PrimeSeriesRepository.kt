package jp.girky.wf_noctuahub.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import jp.girky.wf_noctuahub.data.api.model.PrimeSeriesData
import jp.girky.wf_noctuahub.utils.CacheUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import noctuahub.composeapp.generated.resources.Res

/**
 * Primeシリーズデータの非同期ロード状態を表すシールドインターフェース
 */
sealed interface PrimeSeriesState {
  /**
   * 初期状態または未読み込み状態
   */
  data object Idle : PrimeSeriesState

  /**
   * ロード中状態
   */
  data object Loading : PrimeSeriesState

  /**
   * 正常取得完了状態
   *
   * @property data 取得されたPrimeSeriesData
   */
  data class Success(val data: PrimeSeriesData) : PrimeSeriesState

  /**
   * エラー発生状態
   *
   * @property error 発生した例外
   * @property cachedData フォールバックなどにより取得されたキャッシュデータが存在する場合は格納される
   */
  data class Error(val error: Throwable, val cachedData: PrimeSeriesData? = null) : PrimeSeriesState
}

/**
 * Primeシリーズ定義データの取得、ローカルキャッシュ管理、およびフォールバック処理を担当するリポジトリ
 *
 * @property httpClient 通信用HttpClient
 * @property appSettings updatedAt等のメタデータを永続化する設定管理
 */
class PrimeSeriesRepository(
  private val httpClient: HttpClient,
  private val appSettings: AppSettings
) {

  private val _state = MutableStateFlow<PrimeSeriesState>(PrimeSeriesState.Idle)
  val state: StateFlow<PrimeSeriesState> = _state.asStateFlow()

  private val jsonParser = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
  }

  companion object {
    const val REMOTE_URL = "https://Girky0815.github.io/noctua-data/prime_series.json"
    const val CACHE_FILE_NAME = "prime_series.json"
    const val ASSET_FILE_PATH = "files/prime_series.json"
  }

  /**
   * Primeシリーズ定義データを取得する。
   *
   * [処理フロー]
   * 1. ローカル保存領域（CacheUtils / AppSettings）から前回保存済みの updatedAt とキャッシュデータを取得する。
   * 2. リモートURLに対して HTTP GET リクエストを発行する。
   * 3. リモートデータの取得に成功した場合:
   *    - リモートの updatedAt がローカルの updatedAt より新しい場合、またはローカルにキャッシュが存在しない場合は、
   *      リモートから取得したデータと updatedAt をローカル保存領域に保存し、そのデータを返却する。
   *    - リモートの updatedAt がローカルの updatedAt と同じか古い場合は、ローカルのキャッシュデータをそのまま返却する。
   * 4. リモートデータの取得に失敗した場合 (オフライン・ネットワークエラー時):
   *    - ローカルに保存済みのキャッシュデータが存在する場合は、そのデータを返却する。
   *    - ローカルにキャッシュデータも存在しない場合 (初回起動時のオフライン等) は、
   *      プロジェクトの静的アセット (files/prime_series.json) を読み込んで返却し、ローカル保存領域にも保存する。
   *
   * @return 取得またはフォールバックされた [PrimeSeriesData]
   */
  suspend fun getPrimeSeriesData(): PrimeSeriesData {
    _state.value = PrimeSeriesState.Loading

    // (1) ローカル保存領域から前回の updatedAt とキャッシュデータを取得
    val localUpdatedAt = appSettings.getPrimeSeriesUpdatedAt()
    var localCachedData: PrimeSeriesData? = null
    val localCacheJson = CacheUtils.loadCacheFile(CACHE_FILE_NAME)
    if (!localCacheJson.isNullOrBlank()) {
      try {
        localCachedData = jsonParser.decodeFromString<PrimeSeriesData>(localCacheJson)
      } catch (e: Exception) {
        println("ローカルキャッシュのデシリアライズに失敗しました: ${e.message}")
      }
    }

    try {
      // (2) リモートURLに対して HTTP GET リクエストを発行
      val remoteJsonString = httpClient.get(REMOTE_URL).body<String>()
      val remoteData = jsonParser.decodeFromString<PrimeSeriesData>(remoteJsonString)

      // (3) リモートデータの取得に成功した場合の判定
      val isRemoteNewer = localCachedData == null ||
        localUpdatedAt.isBlank() ||
        remoteData.updatedAt > localUpdatedAt

      val finalData = if (isRemoteNewer) {
        // リモートの updatedAt が新しい、またはキャッシュが存在しない場合は保存
        CacheUtils.saveCacheFile(CACHE_FILE_NAME, remoteJsonString)
        appSettings.setPrimeSeriesUpdatedAt(remoteData.updatedAt)
        remoteData
      } else {
        // リモートがローカルと同じか古い場合はローカルキャッシュを使用
        localCachedData ?: remoteData
      }

      _state.value = PrimeSeriesState.Success(finalData)
      return finalData
    } catch (remoteError: Throwable) {
      println("リモートPrimeシリーズデータの取得に失敗しました: ${remoteError.message}")

      // (4) リモートデータの取得に失敗した場合
      if (localCachedData != null) {
        // 保存済みのキャッシュが存在する場合は返却
        _state.value = PrimeSeriesState.Success(localCachedData)
        return localCachedData
      }

      // ローカルキャッシュが存在しない場合は静的アセットから読み込む
      return try {
        val assetData = loadAssetFallback()
        _state.value = PrimeSeriesState.Success(assetData)
        assetData
      } catch (assetError: Throwable) {
        println("静的アセットの読み込みにも失敗しました: ${assetError.message}")
        _state.value = PrimeSeriesState.Error(remoteError)
        throw remoteError
      }
    }
  }

  /**
   * 静的アセット (composeResources/files/prime_series.json) を読み込んで
   * ローカル保存領域に保存した上で [PrimeSeriesData] を返す
   */
  @OptIn(ExperimentalResourceApi::class)
  private suspend fun loadAssetFallback(): PrimeSeriesData {
    val bytes = Res.readBytes(ASSET_FILE_PATH)
    val jsonString = bytes.decodeToString()
    val assetData = jsonParser.decodeFromString<PrimeSeriesData>(jsonString)

    // ローカル保存領域にも保存
    CacheUtils.saveCacheFile(CACHE_FILE_NAME, jsonString)
    appSettings.setPrimeSeriesUpdatedAt(assetData.updatedAt)

    return assetData
  }
}
