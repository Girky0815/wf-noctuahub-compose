package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import jp.girky.wf_noctuahub.platform.getAppUpdater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Serializable
data class GitHubRelease(
    @SerialName("tag_name") val tagName: String,
    val body: String? = null,
    val assets: List<GitHubAsset> = emptyList()
)

@Serializable
data class GitHubAsset(
    val name: String,
    @SerialName("browser_download_url") val browserDownloadUrl: String,
    val size: Long = 0
)

enum class UpdateStatus {
    CHECKING,
    UP_TO_DATE,
    UPDATE_AVAILABLE,
    DOWNLOADING,
    READY_TO_INSTALL,
    INSTALLING,
    ERROR
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UpdatePage(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val appUpdater = remember { getAppUpdater() }
    val currentVersion = remember { appUpdater.getAppVersionName() }
    val isAndroid = remember { appUpdater.isAndroid }

    var status by remember { mutableStateOf(UpdateStatus.CHECKING) }
    var latestVersionName by remember { mutableStateOf("") }
    var releaseNotes by remember { mutableStateOf("") }
    var downloadUrl by remember { mutableStateOf("") }
    var downloadProgress by remember { mutableStateOf(0f) }
    var downloadedSizeStr by remember { mutableStateOf("") }
    var totalSizeStr by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var lastCheckedTimeStr by remember { mutableStateOf("") }
    var localApkOrExePath by remember { mutableStateOf("") }

    // インストール権限の監視State
    var hasInstallPermission by remember { mutableStateOf(appUpdater.checkInstallPermission()) }

    // セマンティックバージョンの比較ロジック
    fun isNewerVersion(current: String, latest: String): Boolean {
        val currClean = current.trim().removePrefix("v").removeSuffix("-dev")
        val lateClean = latest.trim().removePrefix("v")
        val currParts = currClean.split(".").map { it.toIntOrNull() ?: 0 }
        val lateParts = lateClean.split(".").map { it.toIntOrNull() ?: 0 }
        for (i in 0 until minOf(currParts.size, lateParts.size)) {
            if (lateParts[i] > currParts[i]) return true
            if (lateParts[i] < currParts[i]) return false
        }
        return lateParts.size > currParts.size
    }

    // 更新チェック処理
    fun checkForUpdates() {
        coroutineScope.launch {
            status = UpdateStatus.CHECKING
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json { 
                        ignoreUnknownKeys = true 
                        coerceInputValues = true
                    })
                }
            }
            try {
                val response: HttpResponse = client.get("https://api.github.com/repos/Girky0815/wf-noctuahub-compose/releases/latest") {
                    header(HttpHeaders.UserAgent, "NoctuaHub-Updater")
                }
                
                if (response.status == HttpStatusCode.OK) {
                    val release = response.body<GitHubRelease>()
                    latestVersionName = release.tagName
                    releaseNotes = release.body ?: "リリースノートはありません。"

                    // プラットフォームごとにアセットを判別
                    val targetAsset = if (isAndroid) {
                        release.assets.find { asset -> asset.name.endsWith(".apk") }
                    } else {
                        release.assets.find { asset -> asset.name.endsWith(".exe") }
                    }

                    if (targetAsset != null) {
                        downloadUrl = targetAsset.browserDownloadUrl
                        
                        if (isNewerVersion(currentVersion, latestVersionName)) {
                            status = UpdateStatus.UPDATE_AVAILABLE
                        } else {
                            status = UpdateStatus.UP_TO_DATE
                        }
                    } else {
                        errorMessage = "該当プラットフォーム用のファイルがリリースに見つかりません。"
                        status = UpdateStatus.ERROR
                    }
                } else {
                    errorMessage = "サーバーエラー: ${response.status}"
                    status = UpdateStatus.ERROR
                }

                // 最終確認時刻を更新
                val nowMs = jp.girky.wf_noctuahub.utils.currentTimeMillis()
                val now = kotlinx.datetime.Instant.fromEpochMilliseconds(nowMs).toLocalDateTime(TimeZone.currentSystemDefault())
                lastCheckedTimeStr = "${now.year}/${now.monthNumber.toString().padStart(2, '0')}/${now.dayOfMonth.toString().padStart(2, '0')} ${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}"
            } catch (e: Exception) {
                errorMessage = e.message ?: "アップデート確認中にエラーが発生しました。"
                status = UpdateStatus.ERROR
            } finally {
                client.close()
            }
        }
    }

    // 更新ダウンロード処理
    fun downloadUpdate() {
        coroutineScope.launch(Dispatchers.IO) {
            status = UpdateStatus.DOWNLOADING
            downloadProgress = 0f
            val client = HttpClient(CIO)
            try {
                val tempDir = java.lang.System.getProperty("java.io.tmpdir")
                val fileName = if (isAndroid) "noctuahub_update.apk" else "noctuahub_update.exe"
                val targetFile = java.io.File(tempDir, fileName)
                
                if (targetFile.exists()) {
                    targetFile.delete()
                }

                client.prepareGet(downloadUrl).execute { response ->
                    val contentLength = response.headers[HttpHeaders.ContentLength]?.toLongOrNull() ?: 0L
                    totalSizeStr = if (contentLength > 0) {
                        "${String.format("%.1f", contentLength.toFloat() / (1024 * 1024))} MB"
                    } else {
                        "不明"
                    }

                    val channel = response.bodyAsChannel()
                    targetFile.outputStream().use { output ->
                        val buffer = ByteArray(65536)
                        var downloaded = 0L
                        while (!channel.isClosedForRead) {
                            val read = channel.readAvailable(buffer)
                            if (read > 0) {
                                output.write(buffer, 0, read)
                                downloaded += read
                                downloadedSizeStr = "${String.format("%.1f", downloaded.toFloat() / (1024 * 1024))} MB"
                                if (contentLength > 0) {
                                    downloadProgress = downloaded.toFloat() / contentLength.toFloat()
                                }
                            } else if (read == -1) {
                                break
                            }
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    localApkOrExePath = targetFile.absolutePath
                    status = UpdateStatus.READY_TO_INSTALL
                    // 権限状況を再チェック
                    hasInstallPermission = appUpdater.checkInstallPermission()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = e.message ?: "ダウンロード中にエラーが発生しました。"
                    status = UpdateStatus.ERROR
                }
            } finally {
                client.close()
            }
        }
    }

    // インストール実行処理
    fun installUpdate() {
        if (isAndroid) {
            if (!appUpdater.checkInstallPermission()) {
                appUpdater.requestInstallPermission()
                // 設定画面から戻ってきたときに検知できるよう、定期的にまたはボタン押下時に再確認させます
                return
            }
            status = UpdateStatus.INSTALLING
            appUpdater.installApk(localApkOrExePath)
        } else {
            status = UpdateStatus.INSTALLING
            appUpdater.installExe(localApkOrExePath)
        }
    }

    // 初回ロード時にアップデートを確認
    LaunchedEffect(Unit) {
        checkForUpdates()
    }

    // バックグラウンドから復帰時等にインストール権限を再チェックするためのEffect
    LaunchedEffect(status) {
        if (status == UpdateStatus.READY_TO_INSTALL) {
            hasInstallPermission = appUpdater.checkInstallPermission()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Google Play システムアップデート風の美しい円形アイコンコンテナ
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (status) {
                        UpdateStatus.UP_TO_DATE -> Icons.Rounded.CheckCircle
                        UpdateStatus.ERROR -> Icons.Rounded.Error
                        UpdateStatus.READY_TO_INSTALL -> Icons.Rounded.FileDownloadDone
                        else -> Icons.Rounded.SystemUpdate
                    },
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // タイトル・ステータス表示
            Text(
                text = when (status) {
                    UpdateStatus.CHECKING -> "アップデートを確認しています..."
                    UpdateStatus.UP_TO_DATE -> "最新の状態です"
                    UpdateStatus.UPDATE_AVAILABLE -> "新しいアップデートが利用可能です"
                    UpdateStatus.DOWNLOADING -> "アップデートをダウンロード中..."
                    UpdateStatus.READY_TO_INSTALL -> "インストールの準備ができました"
                    UpdateStatus.INSTALLING -> "更新を適用しています..."
                    UpdateStatus.ERROR -> "アップデートエラー"
                },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // サブ情報表示エリア
            when (status) {
                UpdateStatus.CHECKING -> {
                    Spacer(modifier = Modifier.height(8.dp))
                    LoadingIndicator(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                UpdateStatus.UP_TO_DATE -> {
                    Text(
                        text = "Noctua Hub バージョン: $currentVersion",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                UpdateStatus.UPDATE_AVAILABLE -> {
                    Text(
                        text = "最新バージョン: $latestVersionName (現在: $currentVersion)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "リリースノート:",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = releaseNotes,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                UpdateStatus.DOWNLOADING -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { downloadProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${(downloadProgress * 100).toInt()}%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "$downloadedSizeStr / $totalSizeStr",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                UpdateStatus.READY_TO_INSTALL -> {
                    val message = if (isAndroid) {
                        if (hasInstallPermission) {
                            "インストールを開始すると、システムによってアップデートが適用され、アプリが自動で再起動します。"
                        } else {
                            "アプリ内アップデートを行うには、「不明なアプリのインストール」権限を許可する必要があります。下のボタンを押して端末設定から許可してください。"
                        }
                    } else {
                        "更新を適用すると、現在のアプリが終了し、自動的に Windows 向け最新インストーラーが起動します。"
                    }
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                UpdateStatus.INSTALLING -> {
                    Text(
                        text = "まもなくアップデートプロセスを開始します...",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                UpdateStatus.ERROR -> {
                    Text(
                        text = errorMessage,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        // 右下の Pill 形状のアクションボタン配置
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // キャンセル/戻るボタン (ダウンロード中・適用中でなければ表示)
            if (status != UpdateStatus.DOWNLOADING && status != UpdateStatus.INSTALLING) {
                TextButton(onClick = onBack) {
                    Text("閉じる")
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            // メインアクションボタン
            if (status != UpdateStatus.DOWNLOADING && status != UpdateStatus.INSTALLING) {
                Button(
                    onClick = {
                        when (status) {
                            UpdateStatus.UP_TO_DATE -> checkForUpdates()
                            UpdateStatus.UPDATE_AVAILABLE -> downloadUpdate()
                            UpdateStatus.READY_TO_INSTALL -> installUpdate()
                            UpdateStatus.ERROR -> checkForUpdates()
                            else -> {}
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = when (status) {
                                UpdateStatus.UP_TO_DATE -> Icons.Default.Refresh
                                UpdateStatus.UPDATE_AVAILABLE -> Icons.Default.Download
                                UpdateStatus.READY_TO_INSTALL -> {
                                    if (isAndroid && !hasInstallPermission) Icons.Default.Settings else Icons.Default.PlayArrow
                                }
                                UpdateStatus.ERROR -> Icons.Default.Refresh
                                else -> Icons.Default.Refresh
                            },
                            contentDescription = null
                        )
                        Text(
                            text = when (status) {
                                UpdateStatus.UP_TO_DATE -> "アップデートを確認"
                                UpdateStatus.UPDATE_AVAILABLE -> "更新をダウンロード"
                                UpdateStatus.READY_TO_INSTALL -> {
                                    if (isAndroid && !hasInstallPermission) "設定へ" else "更新を適用"
                                }
                                UpdateStatus.ERROR -> "再試行"
                                else -> "確認"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
