package jp.girky.wf_noctuahub.platform

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import jp.girky.wf_noctuahub.NoctuaApp
import java.io.OutputStream

/**
 * Androidプラットフォーム専用のPlatformUtils実装クラス
 */
class AndroidPlatformUtils : PlatformUtils {

  private val context: Context
    get() = NoctuaApp.getContext()

  override fun saveImageToGallery(image: ImageBitmap, fileName: String): Boolean {
    return try {
      val bitmap = image.asAndroidBitmap()
      val filename = "${fileName}.png"
      
      var outputStream: OutputStream? = null
      var imageUri: Uri? = null
      
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
          put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
          put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
          put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/NoctuaHub")
        }
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (imageUri != null) {
          outputStream = resolver.openOutputStream(imageUri)
        }
      } else {
        // Android 9以下向けフォールバック
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/NoctuaHub"
        val dir = java.io.File(imagesDir)
        if (!dir.exists()) {
          dir.mkdirs()
        }
        val file = java.io.File(dir, filename)
        outputStream = java.io.FileOutputStream(file)
        
        // ギャラリーにスキャンさせる
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = Uri.fromFile(file)
        context.sendBroadcast(mediaScanIntent)
      }
      
      if (outputStream != null) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        true
      } else {
        false
      }
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  override fun copyImageToClipboard(image: ImageBitmap): Boolean {
    // Androidでは画像データを単純なテキストクリップボードにコピーすることは一般的ではないため、
    // ここではサポート対象外（false）とします。UI側でAndroidの場合は「クリップボードへコピー」を非表示にします。
    return false
  }

  override fun openBrowser(url: String) {
    try {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}

actual fun getPlatformUtils(): PlatformUtils = AndroidPlatformUtils()
