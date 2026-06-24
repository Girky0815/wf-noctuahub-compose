package jp.girky.wf_noctuahub.platform

import androidx.compose.runtime.Composable
import kotlin.system.exitProcess

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // デスクトップ環境では物理的な戻るボタンがないため何もしません
}

@Composable
actual fun rememberAppExiter(): () -> Unit {
    return {
        exitProcess(0)
    }
}
