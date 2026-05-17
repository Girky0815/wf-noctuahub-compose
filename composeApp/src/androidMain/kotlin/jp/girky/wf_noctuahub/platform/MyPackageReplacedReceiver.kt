package jp.girky.wf_noctuahub.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyPackageReplacedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            // アプリがアップデートされた瞬間に自動でメイン画面を起動して再起動
            val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(launchIntent)
            }
        }
    }
}
