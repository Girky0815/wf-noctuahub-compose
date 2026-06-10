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

  private fun getBooleanFlow(key: String, defaultValue: Boolean): Flow<Boolean> = callbackFlow {
    val listener = pref.addBooleanListener(key, defaultValue) {
      trySend(it)
    }
    awaitClose { listener.deactivate() }
  }.onStart { emit(pref.getBoolean(key, defaultValue)) }

  val themeModeFlow: Flow<ThemeMode> = getStringFlow("theme_mode", ThemeMode.SYSTEM_DEFAULT.name)
    .map { 
      try { ThemeMode.valueOf(it) } catch (e: Exception) { ThemeMode.SYSTEM_DEFAULT }
    }

  val seedColorFlow: Flow<Int> = getIntFlow("seed_color", 0xFF6750A4.toInt())
  val isDynamicColorFlow: Flow<Boolean> = getBooleanFlow("is_dynamic_color", true)
  val cetusOffsetFlow: Flow<Int> = getIntFlow("cetus_offset", 0)
  val vallisOffsetFlow: Flow<Int> = getIntFlow("vallis_offset", 0)

  fun setThemeMode(mode: ThemeMode) {
    pref.putString("theme_mode", mode.name)
  }

  fun setSeedColor(color: Int) {
    pref.putInt("seed_color", color)
  }

  fun setDynamicColor(enabled: Boolean) {
    pref.putBoolean("is_dynamic_color", enabled)
  }

  fun setCetusOffset(offset: Int) {
    pref.putInt("cetus_offset", offset)
  }

  fun setVallisOffset(offset: Int) {
    pref.putInt("vallis_offset", offset)
  }

  fun getLastManifest(): String {
    return pref.getString("last_manifest", "")
  }

  fun setLastManifest(manifest: String) {
    pref.putString("last_manifest", manifest)
  }

  fun getLastAppVersion(): String {
    return pref.getString("last_app_version", "")
  }

  fun setLastAppVersion(version: String) {
    pref.putString("last_app_version", version)
  }
  
  fun exportSettingsAsMap(): Map<String, String> {
    return mapOf(
      "theme_mode" to pref.getString("theme_mode", ThemeMode.SYSTEM_DEFAULT.name),
      "seed_color" to pref.getInt("seed_color", 0xFF6750A4.toInt()).toString(),
      "is_dynamic_color" to pref.getBoolean("is_dynamic_color", true).toString(),
      "cetus_offset" to pref.getInt("cetus_offset", 0).toString(),
      "vallis_offset" to pref.getInt("vallis_offset", 0).toString()
    )
  }

  fun importSettingsFromMap(map: Map<String, String>) {
    map["theme_mode"]?.let {
      try { setThemeMode(ThemeMode.valueOf(it)) } catch (e: Exception) {}
    }
    map["seed_color"]?.toIntOrNull()?.let { setSeedColor(it) }
    map["is_dynamic_color"]?.toBooleanStrictOrNull()?.let { setDynamicColor(it) }
    map["cetus_offset"]?.toIntOrNull()?.let { setCetusOffset(it) }
    map["vallis_offset"]?.toIntOrNull()?.let { setVallisOffset(it) }
  }
}
