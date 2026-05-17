package jp.girky.wf_noctuahub.platform

interface AppUpdater {
    val isAndroid: Boolean
    fun getAppVersionName(): String
    fun getAppVersionCode(): Long
    fun checkInstallPermission(): Boolean = true
    fun requestInstallPermission() {}
    fun installApk(apkFilePath: String) {}
    fun installExe(exeFilePath: String) {}
}

expect fun getAppUpdater(): AppUpdater
