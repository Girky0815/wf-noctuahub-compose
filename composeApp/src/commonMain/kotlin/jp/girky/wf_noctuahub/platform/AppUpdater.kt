package jp.girky.wf_noctuahub.platform

import kotlinx.coroutines.flow.StateFlow

enum class InstallProgress {
  IDLE,
  INSTALLING,
  SUCCESS,
  CANCELLED,
  FAILED
}

interface AppUpdater {
  val isAndroid: Boolean
  val installProgress: StateFlow<InstallProgress>
  fun getAppVersionName(): String
  fun getAppVersionCode(): Long
  fun checkInstallPermission(): Boolean = true
  fun requestInstallPermission() {}
  fun installApk(apkFilePath: String) {}
  fun installExe(exeFilePath: String) {}
  fun setInstallProgress(progress: InstallProgress) {}
}

expect fun getAppUpdater(): AppUpdater
