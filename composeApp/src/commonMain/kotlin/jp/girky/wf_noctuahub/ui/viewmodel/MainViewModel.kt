package jp.girky.wf_noctuahub.ui.viewmodel

import jp.girky.wf_noctuahub.data.api.model.ExportRegion
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.repository.WarframeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import jp.girky.wf_noctuahub.utils.Translations
import jp.girky.wf_noctuahub.data.repository.DownloadProgress

enum class FetchState {
  IDLE, LOADING_WORLDSTATE, LOADING_EXPORT, SUCCESS, ERROR
}

class MainViewModel(val repository: WarframeRepository) {
  
  // 現在のフェッチ状態
  private val _fetchState = MutableStateFlow(FetchState.IDLE)
  val fetchState: StateFlow<FetchState> = _fetchState.asStateFlow()

  // 初回初期化完了フラグ（Public Exportのロードまで完了したか）
  private val _isInitialized = MutableStateFlow(false)
  val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

  // 最後に発生したエラー
  private val _errorMessage = MutableStateFlow<String?>(null)
  val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

  // 詳細な進捗メッセージ
  private val _loadingMessage = MutableStateFlow<String?>("初期化中...")
  val loadingMessage: StateFlow<String?> = _loadingMessage.asStateFlow()

  // ダウンロード確認用
  private val _showDownloadPrompt = MutableStateFlow(false)
  val showDownloadPrompt: StateFlow<Boolean> = _showDownloadPrompt.asStateFlow()

  private val _downloadTotalSize = MutableStateFlow(0L)
  val downloadTotalSize: StateFlow<Long> = _downloadTotalSize.asStateFlow()

  private var downloadConfirmResult: kotlinx.coroutines.CompletableDeferred<Boolean>? = null

  // ダウンロード進捗ダイアログ用
  private val _showDownloadProgress = MutableStateFlow(false)
  val showDownloadProgress: StateFlow<Boolean> = _showDownloadProgress.asStateFlow()

  private val _downloadProgressState = MutableStateFlow<DownloadProgress?>(null)
  val downloadProgressState: StateFlow<DownloadProgress?> = _downloadProgressState.asStateFlow()

  fun confirmDownload(allow: Boolean) {
    downloadConfirmResult?.complete(allow)
  }

  val worldState: StateFlow<WorldStateResponse?> = repository.worldState

  /**
   * 内部ID（uniqueName）を日本語に変換する
   */
  fun localize(uniqueName: String?): String {
    if (uniqueName == null) return ""
    return repository.localize(uniqueName)
  }

  fun getRegionInfo(uniqueName: String?): ExportRegion? {
    if (uniqueName == null) return null
    return repository.getRegionInfo(uniqueName)
  }

  fun getModDescription(uniqueName: String?): String? {
    if (uniqueName == null) return null
    return repository.getModDescription(uniqueName)
  }

  fun getModCompat(uniqueName: String?): String? {
    if (uniqueName == null) return null
    return repository.getModCompat(uniqueName)?.let { Translations.translateModTarget(it) }
  }

  /**
   * WorldState と Public Export 辞書を一括で初期ロードする
   */
  fun loadInitialData(scope: CoroutineScope) {
    if (_fetchState.value == FetchState.LOADING_WORLDSTATE || _fetchState.value == FetchState.LOADING_EXPORT) return

    scope.launch(Dispatchers.Default) {
      try {
        _errorMessage.value = null
        // 1. WorldState のフェッチ (軽量)
        _loadingMessage.value = "最新のゲーム状況を取得中..."
        _fetchState.value = FetchState.LOADING_WORLDSTATE
        repository.refreshWorldState()

        // 2. Public Export のローカライズ辞書フェッチ (重量)
        _loadingMessage.value = "Public Export が最新か確認中..."
        _fetchState.value = FetchState.LOADING_EXPORT
        
        repository.initializeLocalization(
          onConfirmDownload = { totalSize ->
            _downloadTotalSize.value = totalSize
            _showDownloadPrompt.value = true
            val deferred = kotlinx.coroutines.CompletableDeferred<Boolean>()
            downloadConfirmResult = deferred
            val allowed = deferred.await()
            _showDownloadPrompt.value = false
            if (allowed) {
              _showDownloadProgress.value = true
            }
            allowed
          },
          onProgress = { progress ->
            _downloadProgressState.value = progress
            _loadingMessage.value = progress.taskName
          }
        )

        _showDownloadProgress.value = false
        _loadingMessage.value = null
        _fetchState.value = FetchState.SUCCESS
        _isInitialized.value = true
      } catch (e: Exception) {
        e.printStackTrace()
        val errorStr = e.toString()
        val errorMsg = e.message ?: ""
        val fullErrorDesc = "$errorStr\n$errorMsg".lowercase()

        val friendlyMessage = when {
          // 1. インターネット未接続 (DNS解決失敗、接続拒否など)
          fullErrorDesc.contains("unknownhostexception") || 
          fullErrorDesc.contains("connectexception") ||
          fullErrorDesc.contains("norouteetohost") ||
          fullErrorDesc.contains("resolv") -> {
            "インターネットに接続されていません。\nWi-Fi、モバイルデータ通信、または機内モードの設定を確認してください。"
          }

          // 2. APIサーバーがダウン (500系)
          fullErrorDesc.contains("503") || 
          fullErrorDesc.contains("500") || 
          fullErrorDesc.contains("502") || 
          fullErrorDesc.contains("504") ||
          fullErrorDesc.contains("service unavailable") ||
          fullErrorDesc.contains("internal server error") -> {
            "Warframe 公式 API サーバーがダウンしています。\nしばらく時間をおいてから再試行してください。"
          }

          // 3. APIへ接続できない (タイムアウトなど)
          fullErrorDesc.contains("timeout") || 
          fullErrorDesc.contains("socket") -> {
            "Warframe API または Public Export サーバーへ接続できません。\nネットワークの通信状況を確認し、再度お試しください。"
          }

          else -> {
            "予期しないエラーが発生しました。"
          }
        }

        val stackTraceExcerpt = e.stackTraceToString().take(250)
        _errorMessage.value = "$friendlyMessage\n\n[エラー詳細]\n$errorStr: $errorMsg\n$stackTraceExcerpt..."
        _fetchState.value = FetchState.ERROR
      }
    }
  }
}
