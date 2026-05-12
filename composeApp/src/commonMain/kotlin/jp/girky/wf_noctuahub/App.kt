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
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Adjust
import androidx.compose.material.icons.rounded.Castle
import androidx.compose.material.icons.rounded.Science
import jp.girky.wf_noctuahub.ui.pages.SettingsPage
import jp.girky.wf_noctuahub.data.repository.AppSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import jp.girky.wf_noctuahub.ui.pages.StatusPage
import jp.girky.wf_noctuahub.ui.pages.FissuresPage
import jp.girky.wf_noctuahub.ui.pages.ArchonHuntPage
import jp.girky.wf_noctuahub.ui.pages.DescendiaPage
import jp.girky.wf_noctuahub.ui.pages.ArchimedeaPage
import kotlinx.coroutines.launch
import jp.girky.wf_noctuahub.ui.theme.AppTheme
import jp.girky.wf_noctuahub.ui.theme.getAccentColor
import jp.girky.wf_noctuahub.ui.viewmodel.FetchState
import jp.girky.wf_noctuahub.ui.viewmodel.MainViewModel
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
enum class Screen(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
  Status("status", Icons.Rounded.Dashboard, "ステータス"),
  Fissures("fissures", Icons.Rounded.Bolt, "亀裂"),
  ArchonHunt("archon", Icons.Rounded.Adjust, "アルコン討伐戦"),
  Descendia("descendia", Icons.Rounded.Castle, "ディセンディア"),
  Archimedea("archimedea", Icons.Rounded.Science, "アルキメデア"),
  Settings("settings", Icons.Rounded.Settings, "設定")
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
                  val isSelected = currentScreen == screen
                  ListTile(
                    title = screen.label,
                    titleColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    titleFontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium,
                    leadingIcon = { 
                      Icon(
                        screen.icon, 
                        contentDescription = screen.label,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                      ) 
                    },
                    containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                    onClick = {
                      currentScreen = screen
                      coroutineScope.launch { drawerState.close() }
                    }
                  )
                }
              }

              SectionTitle(title = "中級者向け", modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
              ListGroup {
                listOf(Screen.ArchonHunt, Screen.Descendia, Screen.Archimedea).forEach { screen ->
                  val isSelected = currentScreen == screen
                  ListTile(
                    title = screen.label,
                    titleColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    titleFontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium,
                    leadingIcon = { 
                      Icon(
                        screen.icon, 
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                      ) 
                    },
                    containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                    onClick = {
                      currentScreen = screen
                      coroutineScope.launch { drawerState.close() }
                    }
                  )
                }
              }

              SectionTitle(title = "システム", modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
              ListGroup {
                val isSelected = currentScreen == Screen.Settings
                ListTile(
                  title = Screen.Settings.label,
                  titleColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                  titleFontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium,
                  leadingIcon = { 
                    Icon(
                      Screen.Settings.icon, 
                      contentDescription = null,
                      tint = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    ) 
                  },
                  containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
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
                Icon(Icons.Rounded.Menu, contentDescription = "Menu")
              }
            },
            actions = {
              worldState?.time?.let { timeSec ->
                val dt = kotlinx.datetime.Instant.fromEpochSeconds(timeSec).toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                val timeStr = "${dt.monthNumber}/${dt.dayOfMonth} ${dt.hour.toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}:${dt.second.toString().padStart(2, '0')}"
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(end = 16.dp)) {
                  Text(text = "タイムスタンプ", style = MaterialTheme.typography.bodyMedium)
                  Text(text = timeStr, style = MaterialTheme.typography.bodyMedium)
                }
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
              val isSelected = currentScreen == screen
              NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { 
                  Text(
                    text = screen.label,
                    fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                  ) 
                },
                selected = isSelected,
                onClick = { currentScreen = screen },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                  selectedIconColor = MaterialTheme.colorScheme.primary,
                  selectedTextColor = MaterialTheme.colorScheme.primary,
                  unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                  unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
              )
            }
          }
        }
      ) { innerPadding ->
        @OptIn(ExperimentalMaterial3Api::class)
        val pullState = androidx.compose.material3.pulltorefresh.rememberPullToRefreshState()
        val isRefreshingState = fetchState == FetchState.LOADING_WORLDSTATE || fetchState == FetchState.LOADING_EXPORT
        @OptIn(ExperimentalMaterial3Api::class)
        androidx.compose.material3.pulltorefresh.PullToRefreshBox(
          state = pullState,
          isRefreshing = isRefreshingState,
          onRefresh = { viewModel.loadInitialData(coroutineScope) },
          indicator = {
              jp.girky.wf_noctuahub.ui.components.ui.ExpressivePullToRefreshIndicator(
                  isRefreshing = isRefreshingState,
                  state = pullState,
                  modifier = Modifier.align(Alignment.TopCenter)
              )
          },
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          if (fetchState == FetchState.ERROR) {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
              Text(text = "エラー: $errorMessage", color = MaterialTheme.colorScheme.error)
            }
          } else if (worldState == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                @OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
                androidx.compose.material3.LoadingIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "データを取得中...", color = MaterialTheme.colorScheme.onBackground)
              }
            }
          } else {
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
              Screen.Descendia -> {
                DescendiaPage(
                  worldState = worldState,
                  onLocalize = { viewModel.localize(it) }
                )
              }
              Screen.Archimedea -> {
                ArchimedeaPage(
                  worldState = worldState,
                  onLocalize = { viewModel.localize(it) }
                )
              }
              Screen.Settings -> {
                SettingsPage(
                  appSettings = appSettings,
                  worldState = worldState,
                  errorMessage = errorMessage,
                  fetchState = fetchState
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