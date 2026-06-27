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
        _loadingMessage.value = "翻訳データをダウンロード中 (これには時間がかかる場合があります)..."
        _fetchState.value = FetchState.LOADING_EXPORT
        repository.initializeLocalization()

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
