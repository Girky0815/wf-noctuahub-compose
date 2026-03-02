package jp.girky.wf_noctuahub.utils

expect object LzmaUtils {
    /**
     * Decompresses the given LZMA-compressed byte array into a UTF-8 String.
     */
    fun decompressLzma(compressedData: ByteArray): String
}
