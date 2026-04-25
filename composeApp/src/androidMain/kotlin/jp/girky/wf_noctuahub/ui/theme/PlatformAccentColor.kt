package jp.girky.wf_noctuahub.ui.theme

import androidx.compose.ui.graphics.Color

actual fun getAccentColor(): Color? {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        return try {
            val context = jp.girky.wf_noctuahub.NoctuaApp.getContext()
            val colorInt = context.getColor(android.R.color.system_accent1_600)
            Color(colorInt)
        } catch (e: Exception) {
            null
        }
    }
    return null
}
