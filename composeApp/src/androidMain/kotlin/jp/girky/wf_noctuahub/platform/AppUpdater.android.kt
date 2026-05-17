package jp.girky.wf_noctuahub.platform

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Build
import android.provider.Settings
import jp.girky.wf_noctuahub.MainActivity
import jp.girky.wf_noctuahub.NoctuaApp
import java.io.File
import java.io.FileInputStream

class AndroidAppUpdater : AppUpdater {
    override val isAndroid: Boolean = true
    private val context: Context
        get() = NoctuaApp.getContext()

    override fun getAppVersionName(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    override fun getAppVersionCode(): Long {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (e: Exception) {
            1L
        }
    }

    override fun checkInstallPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.canRequestPackageInstalls()
        } else {
            true
        }
    }

    override fun requestInstallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                data = Uri.parse("package:${context.packageName}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun installApk(apkFilePath: String) {
        val file = File(apkFilePath)
        if (!file.exists()) return

        try {
            val packageInstaller = context.packageManager.packageInstaller
            val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            
            val sessionId = packageInstaller.createSession(params)
            val session = packageInstaller.openSession(sessionId)

            session.openWrite("NoctuaHubUpdate", 0, file.length()).use { outputStream ->
                FileInputStream(file).use { inputStream ->
                    val buffer = ByteArray(65536)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                    session.fsync(outputStream)
                }
            }

            // 動的レシーバーによるインストールステータスの監視とシステム確認ダイアログの前面起動
            val action = "jp.girky.wf_noctuahub.INSTALL_STATUS"
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(receiverContext: Context, intent: Intent) {
                    val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE)
                    when (status) {
                        PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                            // ユーザー確認（インストールダイアログ）が必要な場合、ダイアログActivityを前面で起動
                            val confirmIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
                            } else {
                                @Suppress("DEPRECATION")
                                intent.getParcelableExtra(Intent.EXTRA_INTENT)
                            }
                            if (confirmIntent != null) {
                                confirmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                receiverContext.startActivity(confirmIntent)
                            }
                        }
                        PackageInstaller.STATUS_SUCCESS -> {
                            // インストール成功！自動的にアプリを再起動
                            val launchIntent = receiverContext.packageManager.getLaunchIntentForPackage(receiverContext.packageName)
                            if (launchIntent != null) {
                                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                receiverContext.startActivity(launchIntent)
                            }
                            try {
                                receiverContext.unregisterReceiver(this)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        else -> {
                            // 失敗またはキャンセル時はレシーバーの登録を解除
                            try {
                                receiverContext.unregisterReceiver(this)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

            // 動的レシーバーの登録（Android 14以降のRECEIVER_EXPORTED安全対策を含む）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(receiver, IntentFilter(action), Context.RECEIVER_EXPORTED)
            } else {
                context.registerReceiver(receiver, IntentFilter(action))
            }

            val intent = Intent(action).apply {
                setPackage(context.packageName)
            }
            
            val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                pendingIntentFlags
            )

            session.commit(pendingIntent.intentSender)
            session.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

actual fun getAppUpdater(): AppUpdater = AndroidAppUpdater()
