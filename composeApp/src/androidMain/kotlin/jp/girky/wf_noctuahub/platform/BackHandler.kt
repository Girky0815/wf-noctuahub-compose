package jp.girky.wf_noctuahub.platform

import android.app.Activity
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled, onBack)
}

@Composable
actual fun rememberAppExiter(): () -> Unit {
    val context = LocalContext.current
    val activity = remember(context) {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                break
            }
            ctx = ctx.baseContext
        }
        ctx as? Activity
    }
    
    var lastBackTime = remember { 0L }
    
    return {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackTime < 2000) {
            activity?.finish()
        } else {
            lastBackTime = currentTime
            Toast.makeText(context, "もう一度戻るを押すと終了します", Toast.LENGTH_SHORT).show()
        }
    }
}
