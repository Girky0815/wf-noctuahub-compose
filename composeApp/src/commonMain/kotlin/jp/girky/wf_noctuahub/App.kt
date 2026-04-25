package jp.girky.wf_noctuahub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import androidx.compose.material.icons.filled.Palette
import androidx.compose.ui.platform.LocalUriHandler
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
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Explore
import jp.girky.wf_noctuahub.ui.pages.SettingsPage
import jp.girky.wf_noctuahub.data.repository.AppSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import jp.girky.wf_noctuahub.ui.pages.StatusPage
import jp.girky.wf_noctuahub.ui.pages.FissuresPage
import jp.girky.wf_noctuahub.ui.pages.ArchonHuntPage
import jp.girky.wf_noctuahub.ui.pages.DescendiaPage
import kotlinx.coroutines.launch
import jp.girky.wf_noctuahub.ui.theme.AppTheme
import jp.girky.wf_noctuahub.ui.theme.getAccentColor
import jp.girky.wf_noctuahub.ui.viewmodel.FetchState
import jp.girky.wf_noctuahub.ui.viewmodel.MainViewModel
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle

enum class Screen(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
  Status("status", Icons.Default.Dashboard, "ステータス"),
  Fissures("fissures", Icons.Default.FlashlightOn, "亀裂"),
  ArchonHunt("archon", Icons.Default.Adjust, "アルコン争奪戦"),
  Archimedea("archimedea", Icons.Default.Explore, "ディセンディア"),
  Settings("settings", Icons.Default.Settings, "設定")
}

@Composable
@Preview
fun App() {
  val coroutineScope = rememberCoroutineScope()
  val appSettings = remember { 
      val s = jp.girky.wf_noctuahub.data.repository.createSettings()
      jp.girky.wf_noctuahub.data.repository.AppSettings(s) 
  }
  
  val themeMode by appSettings.themeModeFlow.collectAsState(jp.girky.wf_noctuahub.utils.ThemeMode.SYSTEM_DEFAULT)
  val seedColorArgb by appSettings.seedColorFlow.collectAsState(0xFF6750A4.toInt())
  val useDynamicColor by appSettings.isDynamicColorFlow.collectAsState(true)
  val seedColor = Color(seedColorArgb.toLong())
  
  val isDark = when (themeMode) {
      jp.girky.wf_noctuahub.utils.ThemeMode.LIGHT -> false
      jp.girky.wf_noctuahub.utils.ThemeMode.DARK, jp.girky.wf_noctuahub.utils.ThemeMode.AMOLED_BLACK -> true
      jp.girky.wf_noctuahub.utils.ThemeMode.SYSTEM_DEFAULT -> androidx.compose.foundation.isSystemInDarkTheme()
  }
  
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

  AppTheme(
    darkTheme = isDark,
    seedColor = seedColor,
    useDynamicColor = useDynamicColor,
    blackTheme = themeMode == jp.girky.wf_noctuahub.utils.ThemeMode.AMOLED_BLACK
  ) {
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
            
            Column(
              modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
            ) {
              SectionTitle(title = "一般情報", modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
              ListGroup {
                listOf(Screen.Status, Screen.Fissures).forEach { screen ->
                  ListTile(
                    title = screen.label,
                    leadingIcon = { Icon(screen.icon, contentDescription = screen.label) },
                    containerColor = if (currentScreen == screen) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                    onClick = {
                      currentScreen = screen
                      coroutineScope.launch { drawerState.close() }
                    }
                  )
                }
              }

              SectionTitle(title = "中級者向け", modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
              ListGroup {
                listOf(Screen.ArchonHunt, Screen.Archimedea).forEach { screen ->
                  ListTile(
                    title = screen.label,
                    leadingIcon = { Icon(screen.icon, contentDescription = null) },
                    containerColor = if (currentScreen == screen) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                    onClick = {
                      currentScreen = screen
                      coroutineScope.launch { drawerState.close() }
                    }
                  )
                }
              }

              SectionTitle(title = "システム", modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
              ListGroup {
                ListTile(
                  title = Screen.Settings.label,
                  leadingIcon = { Icon(Screen.Settings.icon, contentDescription = null) },
                  containerColor = if (currentScreen == Screen.Settings) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                  onClick = {
                    currentScreen = Screen.Settings
                    coroutineScope.launch { drawerState.close() }
                  }
                )
              }
              
              Spacer(modifier = Modifier.height(24.dp))
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
            listOf(Screen.Status, Screen.Fissures, Screen.Settings).forEach { screen ->
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
              Screen.ArchonHunt -> {
                ArchonHuntPage(
                  worldState = worldState,
                  onLocalize = { viewModel.localize(it) },
                  onGetRegionInfo = { viewModel.getRegionInfo(it) }
                )
              }
              Screen.Archimedea -> {
                DescendiaPage(
                  worldState = worldState,
                  onLocalize = { viewModel.localize(it) }
                )
              }
              Screen.Settings -> {
                SettingsPage(
                  appSettings = appSettings
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