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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Menu
import jp.girky.wf_noctuahub.ui.pages.SettingsPage
import jp.girky.wf_noctuahub.ui.pages.StatusPage
import jp.girky.wf_noctuahub.ui.pages.FissuresPage
import kotlinx.coroutines.launch
import jp.girky.wf_noctuahub.ui.theme.AppTheme
import jp.girky.wf_noctuahub.ui.theme.getAccentColor
import jp.girky.wf_noctuahub.ui.viewmodel.FetchState
import jp.girky.wf_noctuahub.ui.viewmodel.MainViewModel

enum class Screen(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
    Status("status", Icons.Default.Dashboard, "ステータス"),
    Fissures("fissures", Icons.Default.FlashlightOn, "亀裂"),
    Settings("settings", Icons.Default.Settings, "設定")
}

@Composable
@Preview
fun App() {
    var isDark by remember { mutableStateOf(false) }
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

    var currentScreen by remember { mutableStateOf(Screen.Status) }

    AppTheme(darkTheme = isDark, seedColor = seedColor) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Text(
                            text = "Noctua Hub",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        HorizontalDivider()
                        Screen.values().forEach { screen ->
                            NavigationDrawerItem(
                                label = { Text(screen.label) },
                                icon = { Icon(screen.icon, contentDescription = screen.label) },
                                selected = currentScreen == screen,
                                onClick = {
                                    currentScreen = screen
                                    coroutineScope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                }
            ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    @OptIn(ExperimentalMaterial3Api::class)
                    TopAppBar(
                        title = { Text(currentScreen.label) },
                        navigationIcon = {
                            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                },
                bottomBar = {
                    NavigationBar {
                        Screen.values().forEach { screen ->
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = screen.label) },
                                label = { Text(screen.label) },
                                selected = currentScreen == screen,
                                onClick = { currentScreen = screen }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                when (fetchState) {
                    FetchState.SUCCESS -> {
                        when (currentScreen) {
                            Screen.Status -> {
                                StatusPage(
                                    worldState = worldState,
                                    onLocalize = { viewModel.localize(it) }
                                )
                            }
                            Screen.Fissures -> {
                                FissuresPage(
                                    worldState = worldState,
                                    onLocalize = { viewModel.localize(it) },
                                    onGetRegionInfo = { viewModel.getRegionInfo(it) }
                                )
                            }
                            Screen.Settings -> {
                                SettingsPage(
                                    isDark = isDark,
                                    onDarkThemeChange = { isDark = it },
                                    seedColor = seedColor,
                                    onSeedColorChange = { seedColor = it }
                                )
                            }
                        }
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
}
}