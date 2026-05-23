package jp.girky.wf_noctuahub.platform

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Build
import android.provider.Settings
import jp.girky.wf_noctuahub.NoctuaApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileInputStream

class AndroidAppUpdater : AppUpdater {
  override val isAndroid: Boolean = true

  private val _installProgress = MutableStateFlow(InstallProgress.IDLE)
  override val installProgress: StateFlow<InstallProgress> = _installProgress

  private val context: Context
    get() = NoctuaApp.getContext()

  override fun getAppVersionName(): String {
    return try {
      val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
      packageInfo.versionName ?: "1.0.0"
    } catch (e: Exception) {
      "1.0.0"
    }
  }

  override fun getAppVersionCode(): Long {
    return try {
      val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
      } else {
        @Suppress("DEPRECATION")
        packageInfo.versionCode.toLong()
      }
    } catch (e: Exception) {
      1L
    }
  }

  override fun checkInstallPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.packageManager.canRequestPackageInstalls()
    } else {
      true
    }
  }

  override fun requestInstallPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
        data = Uri.parse("package:${context.packageName}")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(intent)
    }
  }

  override fun installApk(apkFilePath: String) {
    val file = File(apkFilePath)
    if (!file.exists()) return

    try {
      _installProgress.value = InstallProgress.INSTALLING
      val packageInstaller = context.packageManager.packageInstaller
      val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
      
      val sessionId = packageInstaller.createSession(params)
      val session = packageInstaller.openSession(sessionId)

      session.openWrite("NoctuaHubUpdate", 0, file.length()).use { outputStream ->
        FileInputStream(file).use { inputStream ->
          val buffer = ByteArray(65536)
          var length: Int
          while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
          }
          session.fsync(outputStream)
        }
      }

      // PackageInstaller からのインストールステータス更新を UpdateStatusReceiver で受信するため、
      // commit に渡す PendingIntent を getBroadcast で作成します。
      val statusIntent = Intent(context, UpdateStatusReceiver::class.java)
      
      val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
      } else {
        PendingIntent.FLAG_UPDATE_CURRENT
      }

      val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        statusIntent,
        pendingIntentFlags
      )

      session.commit(pendingIntent.intentSender)
      session.close()
    } catch (e: Exception) {
      _installProgress.value = InstallProgress.FAILED
      e.printStackTrace()
    }
  }

  override fun setInstallProgress(progress: InstallProgress) {
    _installProgress.value = progress
  }
}

actual fun getAppUpdater(): AppUpdater = AndroidAppUpdater()
