package jp.girky.wf_noctuahub.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import kotlinx.serialization.json.Json

class WarframeApiClient(private val platformInfo: String = "pc") {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                explicitNulls = false
            })
        }
    }

    suspend fun fetchWorldState(): WorldStateResponse {
        val path = if (platformInfo == "pc") "" else "${platformInfo}/"
        val url = "https://content.warframe.com/dynamic/worldState.php"
        // Alternatively we can use "https://api.warframe.com/cdn/worldState.php"
        // Actually, PC doesn't need platform specifier typically, or it is baked into the URL on mobile/consoles.
        return client.get(url).body()
    }

}
