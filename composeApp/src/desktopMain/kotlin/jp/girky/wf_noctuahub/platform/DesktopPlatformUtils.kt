package jp.girky.wf_noctuahub.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAwtImage
import java.awt.Image
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * JVMで動作する画像をクリップボードにコピーするためのTransferable実装クラス
 */
class ImageSelection(private val image: Image) : Transferable {
  override fun getTransferDataFlavors(): Array<DataFlavor> {
    return arrayOf(DataFlavor.imageFlavor)
  }
  
  override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
    return DataFlavor.imageFlavor.equals(flavor)
  }
  
  override fun getTransferData(flavor: DataFlavor): Any {
    if (isDataFlavorSupported(flavor)) {
      return image
    }
    throw UnsupportedFlavorException(flavor)
  }
}

/**
 * Windows (Desktop JVM) 用のPlatformUtils実装クラス
 */
class DesktopPlatformUtils : PlatformUtils {

  override fun saveImageToGallery(image: ImageBitmap, fileName: String): Boolean {
    return try {
      val bufferedImage = image.asAwtImage()
      val fileChooser = JFileChooser().apply {
        dialogTitle = "画像を保存"
        fileFilter = FileNameExtensionFilter("PNG 画像", "png")
        selectedFile = File("${fileName}.png")
      }
      
      val userSelection = fileChooser.showSaveDialog(null)
      if (userSelection == JFileChooser.APPROVE_OPTION) {
        var file = fileChooser.selectedFile
        if (!file.name.endsWith(".png", ignoreCase = true)) {
          file = File(file.absolutePath + ".png")
        }
        ImageIO.write(bufferedImage, "png", file)
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
    return try {
      val awtImage = image.asAwtImage()
      val selection = ImageSelection(awtImage)
      Toolkit.getDefaultToolkit().systemClipboard.setContents(selection, null)
      true
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  override fun openBrowser(url: String) {
    try {
      if (java.awt.Desktop.isDesktopSupported()) {
        val desktop = java.awt.Desktop.getDesktop()
        if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
          desktop.browse(java.net.URI(url))
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}

actual fun getPlatformUtils(): PlatformUtils = DesktopPlatformUtils()
