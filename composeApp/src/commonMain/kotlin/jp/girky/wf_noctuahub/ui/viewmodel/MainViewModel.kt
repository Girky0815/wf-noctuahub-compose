package jp.girky.wf_noctuahub.ui.viewmodel

import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.repository.WarframeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class FetchState {
    IDLE, LOADING_WORLDSTATE, LOADING_EXPORT, SUCCESS, ERROR
}

class MainViewModel(val repository: WarframeRepository) {
    
    // 現在のフェッチ状態
    private val _fetchState = MutableStateFlow(FetchState.IDLE)
    val fetchState: StateFlow<FetchState> = _fetchState.asStateFlow()

    // 最後に発生したエラー
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val worldState: StateFlow<WorldStateResponse?> = repository.worldState

    /**
     * 内部ID（uniqueName）を日本語に変換する
     */
    fun localize(uniqueName: String?): String {
        if (uniqueName.isNullOrBlank()) return "不明なノード"
        return repository.localize(uniqueName)
    }

    /**
     * WorldState と Public Export 辞書を一括で初期ロードする
     */
    fun loadInitialData(scope: CoroutineScope) {
        if (_fetchState.value == FetchState.LOADING_WORLDSTATE || _fetchState.value == FetchState.LOADING_EXPORT) return

        scope.launch(Dispatchers.Default) {
            try {
                // 1. WorldState のフェッチ
                _fetchState.value = FetchState.LOADING_WORLDSTATE
                repository.refreshWorldState()

                // 2. Public Export のローカライズ辞書フェッチ
                _fetchState.value = FetchState.LOADING_EXPORT
                repository.initializeLocalization()

                _fetchState.value = FetchState.SUCCESS
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = e.message ?: "Unknown error"
                _fetchState.value = FetchState.ERROR
            }
        }
    }
}
