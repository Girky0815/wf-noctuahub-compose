package jp.girky.wf_noctuahub.platform

import androidx.compose.runtime.Composable

/**
 * プラットフォーム固有の戻るボタンハンドラー
 */
@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)

/**
 * アプリを終了するためのプラットフォーム固有の処理
 */
@Composable
expect fun rememberAppExiter(): () -> Unit
