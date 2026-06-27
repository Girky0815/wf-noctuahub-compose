package jp.girky.wf_noctuahub.utils

import kotlinx.serialization.Serializable
import com.materialkolor.PaletteStyle

/**
 * アプリのテーマモードを定義する列挙型。
 */
@Serializable
enum class ThemeMode(val label: String) {
  SYSTEM_DEFAULT("システム(OS)に連動"),
  LIGHT("ライトモード"),
  DARK("ダークモード"),
  AMOLED_BLACK("AMOLED ブラック")
}

/**
 * アプリのカラーパレットスタイルを定義する列挙型。
 */
@Serializable
enum class AppThemeStyle(val label: String, val style: PaletteStyle) {
  TONAL_SPOT("Tonal Spot (標準)", PaletteStyle.TonalSpot),
  NEUTRAL("Neutral (低彩度)", PaletteStyle.Neutral),
  VIBRANT("Vibrant (高彩度)", PaletteStyle.Vibrant),
  EXPRESSIVE("Expressive (高コントラスト個性派)", PaletteStyle.Expressive),
  MONOCHROME("Monochrome (白黒のみ)", PaletteStyle.Monochrome)
}

/**
 * アプリのコントラスト設定を定義する列挙型。
 */
@Serializable
enum class AppThemeContrast(val label: String, val value: Double) {
  LOW("低", -0.5),
  MEDIUM("中 (標準)", 0.0),
  HIGH("高", 0.5)
}

