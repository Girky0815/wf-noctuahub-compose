package jp.girky.wf_noctuahub

import android.os.Bundle
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import android.graphics.Color


class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge(
      // システムナビゲーションバー部分の背景透過設定
        navigationBarStyle = SystemBarStyle.auto(
          lightScrim = Color.TRANSPARENT,
          darkScrim = Color.TRANSPARENT
        )
    )
    super.onCreate(savedInstanceState)
    
    // Android 10 (API 29) 以降における 3 ボタンナビゲーションの強制 Scrim を無効化
//    最小SDKがそもそもこれより上なのでif不要
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      window.isNavigationBarContrastEnforced = false
//    }
    
    setContent {
      App()
    }
  }
}

@Preview
@Composable
fun AppAndroidPreview() {
  App()
}