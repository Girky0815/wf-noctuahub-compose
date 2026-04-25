package jp.girky.wf_noctuahub.data.repository

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

actual fun createSettings(): ObservableSettings {
    val preferences = Preferences.userRoot().node("NoctuaHub")
    return PreferencesSettings(preferences)
}
