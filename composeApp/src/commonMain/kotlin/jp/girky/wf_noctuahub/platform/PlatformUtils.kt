package jp.girky.wf_noctuahub.platform

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Android/Windowsそれぞれで異なる処理（画像保存、クリップボード、ブラウザ）を抽象化するインターフェース
 */
interface PlatformUtils {
  fun saveImageToGallery(image: ImageBitmap, fileName: String): Boolean
  fun copyImageToClipboard(image: ImageBitmap): Boolean
  fun openBrowser(url: String)
}

/**
 * 各プラットフォームで実装を提供するためのプロバイダー
 */
expect fun getPlatformUtils(): PlatformUtils
