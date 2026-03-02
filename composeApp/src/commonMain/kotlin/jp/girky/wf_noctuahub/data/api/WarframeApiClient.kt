package jp.girky.wf_noctuahub.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.serialization.kotlinx.json.json
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.utils.LzmaUtils
import kotlinx.serialization.json.Json

class WarframeApiClient(private val platformInfo: String = "pc") {

    val client = HttpClient {
        install(io.ktor.client.plugins.UserAgent) {
            agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 NoctuaHub/1.0"
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                },
                contentType = io.ktor.http.ContentType.Any
            )
        }
    }

    suspend fun fetchWorldState(): WorldStateResponse {
        val path = if (platformInfo == "pc") "" else "${platformInfo}/"
        val url = "https://content.warframe.com/dynamic/worldState.php"
        // Alternatively we can use "https://api.warframe.com/cdn/worldState.php"
        // Actually, PC doesn't need platform specifier typically, or it is baked into the URL on mobile/consoles.
        return client.get(url).body()
    }

    /**
     * Public Export 共通の Base URL.
     */
    val publicExportBaseUrl = "https://origin.warframe.com/PublicExport"

    /**
     * 日本語のマニフェスト (index_ja.txt.lzma) をフェッチして解凍し、各Exportファイルへのパス一覧(文字列リスト)を返す。
     */
    suspend fun fetchPublicExportManifest(): List<String> {
        val url = "$publicExportBaseUrl/index_ja.txt.lzma"
        val response = client.get(url)
        println("fetchPublicExportManifest: status=${response.status}")
        val responseBytes = response.readBytes()
        println("fetchPublicExportManifest: size=${responseBytes.size}, head=${responseBytes.take(5).joinToString(",") { it.toUByte().toString(16) }}")
        val decompressedText = LzmaUtils.decompressLzma(responseBytes)
        // Each line looks like:
        // ExportResources_ja.json!00_Q+J1...
        return decompressedText.lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

    /**
     * マニフェスト内のファイル行（例：`ExportResources_ja.json!00_Q+J1...`）から
     * JSONファイル(非圧縮)を取得してデシリアライズする。
     * T: T は ExportResources などを表す Serializable なデータクラス。
     */
    suspend inline fun <reified T> fetchExportJson(manifestLine: String): T {
        val cleanManifestLine = manifestLine.trim()
        val url = "http://content.warframe.com/PublicExport/Manifest/$cleanManifestLine"
        println("fetchExportJson REQUEST URL: $url")
        val response = client.get(url)
        println("fetchExportJson ($cleanManifestLine): status=${response.status}")
        
        val jsonString = response.readBytes().decodeToString()

        val jsonParser = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        return jsonParser.decodeFromString<T>(jsonString)
    }

}
