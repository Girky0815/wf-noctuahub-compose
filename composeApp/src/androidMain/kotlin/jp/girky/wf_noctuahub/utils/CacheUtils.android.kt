package jp.girky.wf_noctuahub.utils

import jp.girky.wf_noctuahub.NoctuaApp
import java.io.File

actual object CacheUtils {
  actual fun saveCacheFile(fileName: String, content: String) {
    try {
      val context = NoctuaApp.getContext()
      val cacheDir = context.cacheDir
      val file = File(cacheDir, fileName)
      file.writeText(content, Charsets.UTF_8)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  actual fun loadCacheFile(fileName: String): String? {
    return try {
      val context = NoctuaApp.getContext()
      val cacheDir = context.cacheDir
      val file = File(cacheDir, fileName)
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
      val context = NoctuaApp.getContext()
      val cacheDir = context.cacheDir
      val file = File(cacheDir, fileName)
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
