package jp.girky.wf_noctuahub.utils

import java.io.File

actual object CacheUtils {
  private fun getCacheDir(): File {
    val userHome = System.getProperty("user.home")
    val dir = File(userHome, ".noctuahub/cache")
    if (!dir.exists()) {
      dir.mkdirs()
    }
    return dir
  }

  actual fun saveCacheFile(fileName: String, content: String) {
    try {
      val file = File(getCacheDir(), fileName)
      file.writeText(content, Charsets.UTF_8)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  actual fun loadCacheFile(fileName: String): String? {
    return try {
      val file = File(getCacheDir(), fileName)
      if (file.exists()) {
        file.readText(Charsets.UTF_8)
      } else {
        null
      }
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  actual fun deleteCacheFile(fileName: String): Boolean {
    return try {
      val file = File(getCacheDir(), fileName)
      if (file.exists()) {
        file.delete()
      } else {
        false
      }
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }
}
