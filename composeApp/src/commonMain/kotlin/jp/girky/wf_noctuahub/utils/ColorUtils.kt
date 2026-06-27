package jp.girky.wf_noctuahub.utils

import androidx.compose.ui.graphics.Color

object ColorUtils {
  /**
   * HSV (色相, 彩度, 明度) から Color を生成する
   */
  fun hsvToColor(hue: Float, saturation: Float = 1.0f, value: Float = 1.0f): Color {
    val h = hue.coerceIn(0f, 360f)
    val s = saturation.coerceIn(0f, 1f)
    val v = value.coerceIn(0f, 1f)
    
    val c = v * s
    val x = c * (1f - kotlin.math.abs((h / 60f) % 2f - 1f))
    val m = v - c
    
    val (r, g, b) = when {
      h < 60f -> Triple(c, x, 0f)
      h < 120f -> Triple(x, c, 0f)
      h < 180f -> Triple(0f, c, x)
      h < 240f -> Triple(0f, x, c)
      h < 300f -> Triple(x, 0f, c)
      else -> Triple(c, 0f, x)
    }
    return Color(
      red = (r + m).coerceIn(0f, 1f),
      green = (g + m).coerceIn(0f, 1f),
      blue = (b + m).coerceIn(0f, 1f),
      alpha = 1.0f
    )
  }

  /**
   * Color から Hue (色相) を逆算する (0.0 - 360.0)
   */
  fun colorToHue(color: Color): Float {
    val r = color.red
    val g = color.green
    val b = color.blue
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min
    if (delta == 0f) return 0f
    val hue = when (max) {
      r -> ((g - b) / delta) % 6f
      g -> ((b - r) / delta) + 2f
      else -> ((r - g) / delta) + 4f
    } * 60f
    return if (hue < 0) hue + 360f else hue
  }
}
