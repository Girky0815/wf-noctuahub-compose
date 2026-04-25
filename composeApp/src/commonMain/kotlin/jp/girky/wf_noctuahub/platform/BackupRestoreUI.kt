package jp.girky.wf_noctuahub.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * プラットフォーム固有のファイルピッカーを利用してバックアップ・復元を行うUIコンポーネント。
 * @param onExport ユーザーが「エクスポート」を押した際に呼ばれる。YAML等を文字列で返し、それをファイルとして保存する。
 * @param onImport ユーザーがファイルを選択した際に呼ばれる。読み込まれたファイル内容（YAML等）が渡される。
 */
@Composable
expect fun BackupRestoreButtons(
    modifier: Modifier = Modifier,
    onExport: suspend () -> String,
    onImport: suspend (String) -> Unit
)
