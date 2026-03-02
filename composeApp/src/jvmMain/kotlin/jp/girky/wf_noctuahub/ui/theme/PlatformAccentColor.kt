package jp.girky.wf_noctuahub.ui.theme

import androidx.compose.ui.graphics.Color
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg

actual fun getAccentColor(): Color? {
    return try {
        // HKEY_CURRENT_USER\Software\Microsoft\Windows\DWM の ColorizationColor から色を取得する
        val colorValue = Advapi32Util.registryGetIntValue(
            WinReg.HKEY_CURRENT_USER,
            "Software\\Microsoft\\Windows\\DWM",
            "ColorizationColor"
        )
        // DWMのカラー値は ARGB 形式(0xAARRGGBB)。
        // ComposeのColorコンストラクタ(Int)は符号なしARGBまたは符号付きIntを取るのでそのまま渡せます。
        // Alpha値を完全な不透明(0xFF)にして使用するのが自然です。
        val alpha = 0xFF
        val red = (colorValue shr 16) and 0xFF
        val green = (colorValue shr 8) and 0xFF
        val blue = colorValue and 0xFF
        Color(red = red, green = green, blue = blue, alpha = alpha)
    } catch (e: Exception) {
        // レジストリキーが見つからない、OSがWindowsでないなどの場合はフォールバック
        null
    }
}
