package jp.girky.wf_noctuahub.utils

import kotlin.math.log10
import kotlin.math.round

object FormatUtils {
  private fun powerOf1024(n: Int): Double {
    var result = 1.0
    for (i in 0 until n) {
      result *= 1024.0
    }
    return result
  }

  private fun powerOfTen(n: Int): Double {
    var result = 1.0
    for (i in 0 until n) {
      result *= 10.0
    }
    return result
  }

  fun formatBytes(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(bytes.toDouble()) / log10(1024.0)).toInt()
    val value = bytes.toDouble() / powerOf1024(digitGroups)
    val rounded = round(value * 10.0) / 10.0
    return "$rounded ${units[digitGroups]}"
  }

  fun Float.format(digits: Int): String {
    val multiplier = powerOfTen(digits)
    val rounded = round(this.toDouble() * multiplier) / multiplier
    return rounded.toString()
  }

  fun Double.format(digits: Int): String {
    val multiplier = powerOfTen(digits)
    val rounded = round(this * multiplier) / multiplier
    return rounded.toString()
  }
}
