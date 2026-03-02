package jp.girky.wf_noctuahub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.WarframeApiClient
import jp.girky.wf_noctuahub.data.repository.WarframeRepository
import jp.girky.wf_noctuahub.ui.pages.StatusPage
import jp.girky.wf_noctuahub.ui.theme.AppTheme
import jp.girky.wf_noctuahub.ui.theme.getAccentColor
import jp.girky.wf_noctuahub.ui.viewmodel.FetchState
import jp.girky.wf_noctuahub.ui.viewmodel.MainViewModel

@Composable
@Preview
fun App() {
    var isDark by remember { mutableStateOf(true) }
    var seedColor by remember { mutableStateOf(getAccentColor() ?: Color.White) }
    val coroutineScope = rememberCoroutineScope()
    
    val apiClient = remember { WarframeApiClient() }
    val repository = remember { WarframeRepository(apiClient) }
    val viewModel = remember { MainViewModel(repository) }

    val fetchState by viewModel.fetchState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val worldState by viewModel.worldState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadInitialData(coroutineScope)
    }

    AppTheme(darkTheme = isDark, seedColor = seedColor) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            Column(modifier = Modifier.fillMaxSize()) {
                // デモ用ヘッダー・テーマ切り替え (後でSettingsへ移行予定)
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = isDark, onCheckedChange = { isDark = it })
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Button(onClick = { seedColor = Color(0xFFE53935) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))) { Text("Red") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { seedColor = Color(0xFF1E88E5) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))) { Text("Blue") }
                }

                when (fetchState) {
                    FetchState.SUCCESS -> {
                        StatusPage(
                            worldState = worldState,
                            onLocalize = { viewModel.localize(it) }
                        )
                    }
                    FetchState.ERROR -> {
                        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                            Text(text = "エラー: $errorMessage", color = MaterialTheme.colorScheme.error)
                        }
                    }
                    else -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "データを取得中...", color = MaterialTheme.colorScheme.onBackground)
                            }
                        }
                    }
                }
            }
        }
    }
}