package jp.girky.wf_noctuahub.platform

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import kotlin.system.exitProcess

class JvmAppUpdater : AppUpdater {
  override val isAndroid: Boolean = false
  
  private val _installProgress = MutableStateFlow(InstallProgress.IDLE)
  override val installProgress: StateFlow<InstallProgress> = _installProgress

  override fun getAppVersionName(): String {
    return "1.0.0"
  }

  override fun getAppVersionCode(): Long {
    return 1L
  }

  override fun installExe(exeFilePath: String) {
    val file = File(exeFilePath)
    if (!file.exists()) return

    try {
      _installProgress.value = InstallProgress.INSTALLING
      // runas 動詞を使用して Windows シェルから管理者権限でインストーラーを安全に起動
      ProcessBuilder("powershell.exe", "-Command", "Start-Process '${file.absolutePath}' -Verb RunAs").start()
      exitProcess(0)
    } catch (e: Exception) {
      _installProgress.value = InstallProgress.FAILED
      e.printStackTrace()
    }
  }

  override fun setInstallProgress(progress: InstallProgress) {
    _installProgress.value = progress
  }
}

actual fun getAppUpdater(): AppUpdater = JvmAppUpdater()
