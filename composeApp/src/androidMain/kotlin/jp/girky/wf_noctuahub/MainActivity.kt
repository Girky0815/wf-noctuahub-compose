package jp.girky.wf_noctuahub

import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import jp.girky.wf_noctuahub.platform.InstallProgress
import jp.girky.wf_noctuahub.platform.getAppUpdater

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        handleIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)
        if (status != -999) {
            val appUpdater = getAppUpdater()
            when (status) {
                PackageInstaller.STATUS_SUCCESS -> {
                    appUpdater.setInstallProgress(InstallProgress.SUCCESS)
                }
                PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                    appUpdater.setInstallProgress(InstallProgress.INSTALLING)
                }
                PackageInstaller.STATUS_FAILURE_ABORTED -> {
                    // ユーザーによるキャンセル！
                    appUpdater.setInstallProgress(InstallProgress.CANCELLED)
                }
                else -> {
                    // その他のエラー・失敗
                    appUpdater.setInstallProgress(InstallProgress.FAILED)
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}