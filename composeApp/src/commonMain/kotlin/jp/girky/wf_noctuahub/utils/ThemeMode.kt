package jp.girky.wf_noctuahub.utils

import kotlinx.serialization.Serializable

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
