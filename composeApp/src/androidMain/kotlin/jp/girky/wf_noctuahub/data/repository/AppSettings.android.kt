package jp.girky.wf_noctuahub.data.repository

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import android.content.Context
import jp.girky.wf_noctuahub.NoctuaApp

actual fun createSettings(): ObservableSettings {
    val context = NoctuaApp.getContext()
    val sharedPrefs = context.getSharedPreferences("noctua_settings", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPrefs)
}
