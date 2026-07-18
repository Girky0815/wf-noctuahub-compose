package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import jp.girky.wf_noctuahub.ui.components.ui.ListItem
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GitHubReleaseHistory(
  @SerialName("tag_name") val tagName: String,
  @SerialName("name") val name: String? = null,
  @SerialName("published_at") val publishedAt: String? = null,
  val body: String? = null
)

sealed interface HistoryState {
  object Loading : HistoryState
  data class Success(val releases: List<GitHubReleaseHistory>) : HistoryState
  data class Error(val message: String) : HistoryState
}

@Composable
fun VersionHistoryPage(
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()
  var state by remember { mutableStateOf<HistoryState>(HistoryState.Loading) }

  fun fetchHistory() {
    coroutineScope.launch {
      state = HistoryState.Loading
      val client = HttpClient(CIO) {
        install(ContentNegotiation) {
          json(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
          })
        }
      }
      try {
        val response: HttpResponse = client.get("https://api.github.com/repos/Girky0815/wf-noctuahub-compose/releases") {
          header(HttpHeaders.UserAgent, "NoctuaHub-Updater")
        }
        if (response.status == HttpStatusCode.OK) {
          val releases = response.body<List<GitHubReleaseHistory>>()
          state = HistoryState.Success(releases)
        } else {
          state = HistoryState.Error("履歴の取得に失敗しました (ステータス: ${response.status})")
        }
      } catch (e: Exception) {
        e.printStackTrace()
        state = HistoryState.Error("通信エラーが発生しました: ${e.message}")
      } finally {
        client.close()
      }
    }
  }

  LaunchedEffect(Unit) {
    fetchHistory()
  }

  Box(
    modifier = modifier.fillMaxSize()
  ) {
    when (val currentState = state) {
      is HistoryState.Loading -> {
        CircularProgressIndicator(
          modifier = Modifier.align(Alignment.Center)
        )
      }
      is HistoryState.Error -> {
        Column(
          modifier = Modifier.fillMaxSize().padding(16.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = currentState.message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 16.dp)
          )
          Button(onClick = { fetchHistory() }) {
            Text("再試行")
          }
        }
      }
      is HistoryState.Success -> {
        val releases = currentState.releases
        if (releases.isEmpty()) {
          Text(
            text = "バージョン履歴が見つかりませんでした。",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.Center)
          )
        } else {
          Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            releases.forEach { release ->
              val dateStr = release.publishedAt?.substringBefore("T") ?: ""
              val displayName = release.name ?: release.tagName
              
              ListItem(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(
                  modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = displayName,
                      style = MaterialTheme.typography.titleLarge,
                      fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    if (dateStr.isNotEmpty()) {
                      Text(
                        text = dateStr,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                  }
                  
                  release.body?.let { bodyText ->
                    if (bodyText.isNotBlank()) {
                      Spacer(modifier = Modifier.height(12.dp))
                      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                      Spacer(modifier = Modifier.height(8.dp))
                      MarkdownText(
                        markdown = bodyText,
                        modifier = Modifier.fillMaxWidth()
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
