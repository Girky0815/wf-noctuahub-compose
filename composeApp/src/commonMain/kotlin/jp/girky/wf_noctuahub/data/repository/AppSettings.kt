package jp.girky.wf_noctuahub.data.repository

import com.russhwolf.settings.ObservableSettings
import jp.girky.wf_noctuahub.utils.ThemeMode
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * 設定（SharedPreferences/UserDefaults等）を生成する関数
 */
expect fun createSettings(): ObservableSettings

/**
 * ユーザー設定を管理するクラス
 */
class AppSettings(private val pref: ObservableSettings) {

    private fun getStringFlow(key: String, defaultValue: String): Flow<String> = callbackFlow {
        val listener = pref.addStringListener(key, defaultValue) {
            trySend(it)
        }
        awaitClose { listener.deactivate() }
    }.onStart { emit(pref.getString(key, defaultValue)) }

    private fun getIntFlow(key: String, defaultValue: Int): Flow<Int> = callbackFlow {
        val listener = pref.addIntListener(key, defaultValue) {
            trySend(it)
        }
        awaitClose { listener.deactivate() }
    }.onStart { emit(pref.getInt(key, defaultValue)) }

    val themeModeFlow: Flow<ThemeMode> = getStringFlow("theme_mode", ThemeMode.SYSTEM_DEFAULT.name)
        .map { 
            try { ThemeMode.valueOf(it) } catch (e: Exception) { ThemeMode.SYSTEM_DEFAULT }
        }

    val seedColorFlow: Flow<Int> = getIntFlow("seed_color", 0xFF6750A4.toInt())

    fun setThemeMode(mode: ThemeMode) {
        pref.putString("theme_mode", mode.name)
    }

    fun setSeedColor(color: Int) {
        pref.putInt("seed_color", color)
    }
    
    fun exportSettingsAsMap(): Map<String, String> {
        return mapOf(
            "theme_mode" to pref.getString("theme_mode", ThemeMode.SYSTEM_DEFAULT.name),
            "seed_color" to pref.getInt("seed_color", 0xFF6750A4.toInt()).toString()
        )
    }

    fun importSettingsFromMap(map: Map<String, String>) {
        map["theme_mode"]?.let {
            try { setThemeMode(ThemeMode.valueOf(it)) } catch (e: Exception) {}
        }
        map["seed_color"]?.toIntOrNull()?.let { setSeedColor(it) }
    }
}
