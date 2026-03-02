package jp.girky.wf_noctuahub.data.repository

import jp.girky.wf_noctuahub.data.api.WarframeApiClient
import jp.girky.wf_noctuahub.data.api.model.ExportCustomsResponse
import jp.girky.wf_noctuahub.data.api.model.ExportRegionsResponse
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WarframeRepository(private val apiClient: WarframeApiClient) {

    private val _worldState = MutableStateFlow<WorldStateResponse?>(null)
    val worldState: StateFlow<WorldStateResponse?> = _worldState.asStateFlow()

    // Public Export の uniqueName (e.g. /Lotus/Language/...) と日本語名のマッピング辞書
    private val localizationDict = mutableMapOf<String, String>()

    /**
     * WorldState をフェッチして内部状態を更新する
     */
    suspend fun refreshWorldState() {
        try {
            val newState = apiClient.fetchWorldState()
            _worldState.value = newState
        } catch (e: Exception) {
            e.printStackTrace()
            // エラー状態の管理などを今後追加する
        }
    }

    /**
     * アプリ起動時に Public Export の各種辞書をダウンロード・解凍・パースしオンメモリに保持する
     */
    suspend fun initializeLocalization() {
        try {
            val manifest = apiClient.fetchPublicExportManifest()
            
            // Customs (一般的なアイテム名やテキスト) の取得
            val customsLine = manifest.find { it.startsWith("ExportCustoms_ja.json") }
            if (customsLine != null) {
                val customsResponse: ExportCustomsResponse = apiClient.fetchExportJson(customsLine)
                customsResponse.exportCustoms?.forEach { item ->
                    localizationDict[item.uniqueName] = item.name
                }
            }

            // Regions (ノード名など)
            val regionsLine = manifest.find { it.startsWith("ExportRegions_ja.json") }
            if (regionsLine != null) {
                val regionsResponse: ExportRegionsResponse = apiClient.fetchExportJson(regionsLine)
                regionsResponse.exportRegions?.forEach { region ->
                    val planet = region.systemName ?: ""
                    val formatted = if (planet.isNotBlank()) "${region.name} ($planet)" else region.name
                    localizationDict[region.uniqueName] = formatted
                }
            }

            // TODO: 必要に応じて Weapons, Warframes, Resources など他の Export データも辞書に追加する
            
            // デバッグ用: 作成した辞書をファイルに出力
            try {
                val tempDir = java.io.File("./temp")
                if (!tempDir.exists()) tempDir.mkdirs()
                val dictFile = java.io.File(tempDir, "localization_dict.json")
                val jsonString = buildString {
                    append("{\n")
                    val entries = localizationDict.entries.toList()
                    entries.forEachIndexed { index, entry ->
                        val safeKey = entry.key.replace("\"", "\\\"")
                        val safeValue = entry.value.replace("\"", "\\\"")
                        append("  \"$safeKey\": \"$safeValue\"")
                        if (index < entries.size - 1) append(",\n") else append("\n")
                    }
                    append("}")
                }
                dictFile.writeText(jsonString)
                println("出力: ${dictFile.absolutePath}")
            } catch (e: Exception) {
                println("辞書出力エラー: ${e.message}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * アイテムの内部名（uniqueName）を日本語に翻訳する。
     * 辞書に存在しない場合はそのまま uniqueName を返すか、末尾の階層だけを返すなどのフォールバックを行う
     */
    fun localize(uniqueName: String): String {
        return localizationDict[uniqueName] ?: uniqueName
    }
}
