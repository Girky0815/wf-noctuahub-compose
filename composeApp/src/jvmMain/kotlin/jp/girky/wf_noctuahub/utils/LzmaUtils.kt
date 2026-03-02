package jp.girky.wf_noctuahub.utils

import org.tukaani.xz.LZMAInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

actual object LzmaUtils {
    actual fun decompressLzma(compressedData: ByteArray): String {
        return try {
            ByteArrayInputStream(compressedData).use { bais ->
                LZMAInputStream(bais).use { lzmaIn ->
                    val out = ByteArrayOutputStream()
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    while (lzmaIn.read(buffer).also { bytesRead = it } != -1) {
                        out.write(buffer, 0, bytesRead)
                    }
                    out.toByteArray().decodeToString()
                }
            }
        } catch (e: Exception) {
            println("LZMA Decompression failed: ${e.message}")
            throw e
        }
    }
}
