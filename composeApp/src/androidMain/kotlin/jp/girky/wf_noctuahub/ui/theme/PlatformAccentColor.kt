package jp.girky.wf_noctuahub.ui.theme

import androidx.compose.ui.graphics.Color

actual fun getAccentColor(): Color? {
    // on Android, MaterialKolor will automatically pick up the system dynamic color in many cases,
    // but here we just return null safely if we don't have a specific way.
    // Actually, Android 12+ dynamic color is handled at the theme level by MaterialKolor natively,
    // so we don't strictly need to extract a single seed color manually for MaterialKolor if we enable its dynamic switch.
    return null
}
