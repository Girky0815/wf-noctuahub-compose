package jp.girky.wf_noctuahub.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Build

/**
 * Android の PackageInstaller からのインストールステータス更新を受信する BroadcastReceiver。
 * ユーザーのインストール承認（ダイアログ表示）や、インストール完了・失敗などの進捗状況を処理します。
 */
class UpdateStatusReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)
        if (status == -999) return

        val appUpdater = getAppUpdater()
        when (status) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                // ユーザーにインストール許可を求める確認画面（システムダイアログ）を最前面で起動
                @Suppress("DEPRECATION")
                val confirmIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
                } else {
                    intent.getParcelableExtra(Intent.EXTRA_INTENT)
                }
                if (confirmIntent != null) {
                    confirmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(confirmIntent)
                }
            }
            PackageInstaller.STATUS_SUCCESS -> {
                // インストール成功
                appUpdater.setInstallProgress(InstallProgress.SUCCESS)
            }
            PackageInstaller.STATUS_FAILURE_ABORTED -> {
                // ユーザーによるキャンセル
                appUpdater.setInstallProgress(InstallProgress.CANCELLED)
            }
            else -> {
                // その他のエラー・失敗
                appUpdater.setInstallProgress(InstallProgress.FAILED)
            }
        }
    }
}
