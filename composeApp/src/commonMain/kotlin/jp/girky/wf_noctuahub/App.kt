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
import jp.girky.wf_noctuahub.data.api.WarframeApiClient
import jp.girky.wf_noctuahub.ui.theme.AppTheme
import jp.girky.wf_noctuahub.ui.theme.getAccentColor
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    var isDark by remember { mutableStateOf(true) }
    var seedColor by remember { mutableStateOf(getAccentColor() ?: Color.White) }
    var apiResultText by remember { mutableStateOf("Fetching API...") }
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val client = WarframeApiClient()
                val ws = client.fetchWorldState()
                val msg = "Success!\nWorldSeed: ${ws.worldSeed}\n" +
                        "Alerts: ${ws.alerts?.size ?: 0}\n" +
                        "Sorties: ${ws.sorties?.size ?: 0}\n" +
                        "Invasions: ${ws.invasions?.size ?: 0}\n" +
                        "Fissures: ${ws.activeMissions?.size ?: 0}"
                println("API FETCH RESULT: $msg")
                apiResultText = msg
            } catch (e: Exception) {
                println("API FETCH ERROR: ${e.message}")
                e.printStackTrace()
                apiResultText = "Error: ${e.message}"
            }
        }
    }

    AppTheme(darkTheme = isDark, seedColor = seedColor) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Noctua Hub Theme Demo", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = isDark, onCheckedChange = { isDark = it })
                    Spacer(modifier = Modifier.width(24.dp))
                    
                    Text("Seed Color:", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { seedColor = Color(0xFFE53935) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))) { Text("Red") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { seedColor = Color(0xFF1E88E5) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))) { Text("Blue") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { seedColor = getAccentColor() ?: Color.White }) { Text("OS Accent/Default") }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Text(text = apiResultText, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                val colorDefinitions = listOf(
                    Triple("Primary", MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary),
                    Triple("Primary Container", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer),
                    Triple("Secondary", MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary),
                    Triple("Secondary Container", MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer),
                    Triple("Tertiary", MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.onTertiary),
                    Triple("Tertiary Container", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer),
                    Triple("Error", MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError),
                    Triple("Error Container", MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer),
                    Triple("Surface", MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface),
                    Triple("Surface Variant", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant),
                    Triple("Surface Container", MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.colorScheme.onSurface),
                    Triple("Surface Container High", MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.colorScheme.onSurface),
                )
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(colorDefinitions) { (name, bgColor, contentColor) ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = bgColor, contentColor = contentColor)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                Text(text = name, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}