package jp.girky.wf_noctuahub.platform

import java.io.File
import kotlin.system.exitProcess

class JvmAppUpdater : AppUpdater {
    override val isAndroid: Boolean = false
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
            ProcessBuilder(file.absolutePath).start()
            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

actual fun getAppUpdater(): AppUpdater = JvmAppUpdater()
