package jp.girky.wf_noctuahub.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import jp.girky.wf_noctuahub.data.repository.AppSettings

/**
 * アプリ内のハプティクス（触覚フィードバック）を一元管理するクラス
 */
class AppHaptics(
  private val hapticFeedback: HapticFeedback,
  val isMasterEnabled: Boolean,
  val isPullToRefreshEnabled: Boolean,
  val isToggleEnabled: Boolean,
  val isMenuEnabled: Boolean
) {
  /**
   * Raw（直接指定）でのハプティクス実行（デバッグ用および任意トリガー用）
   */
  fun trigger(type: HapticFeedbackType, ignoreSettings: Boolean = false) {
    if (ignoreSettings || isMasterEnabled) {
      try {
        hapticFeedback.performHapticFeedback(type)
      } catch (_: Throwable) {
        // プラットフォーム未対応等の例外は安全に無視
      }
    }
  }

  /**
   * Pull-to-Refresh で更新のしきい値を超えた時
   */
  fun triggerPullRefreshThreshold() {
    if (isMasterEnabled && isPullToRefreshEnabled) {
      try {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
      } catch (_: Throwable) {
        trigger(HapticFeedbackType.TextHandleMove)
      }
    }
  }

  /**
   * Pull-to-Refresh などの更新処理が完了した時
   */
  fun triggerPullRefreshComplete() {
    if (isMasterEnabled && isPullToRefreshEnabled) {
      try {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
      } catch (_: Throwable) {
        trigger(HapticFeedbackType.LongPress)
      }
    }
  }

  /**
   * スイッチやトグルの ON/OFF 切り替え時
   */
  fun triggerToggle(isOn: Boolean) {
    if (isMasterEnabled && isToggleEnabled) {
      try {
        if (isOn) {
          hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
        } else {
          hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
        }
      } catch (_: Throwable) {
        trigger(HapticFeedbackType.TextHandleMove)
      }
    }
  }

  /**
   * メニュー（ドロワー）が開いた時（ToggleOn 相当）
   */
  fun triggerMenuOpen() {
    if (isMasterEnabled && isMenuEnabled) {
      try {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
      } catch (_: Throwable) {
        trigger(HapticFeedbackType.TextHandleMove)
      }
    }
  }

  /**
   * メニュー（ドロワー）が閉じた時（ToggleOff 相当）
   */
  fun triggerMenuClose() {
    if (isMasterEnabled && isMenuEnabled) {
      try {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
      } catch (_: Throwable) {
        trigger(HapticFeedbackType.TextHandleMove)
      }
    }
  }
}

/**
 * Composable 内で AppHaptics を取得・購読するためのヘルパー
 */
@Composable
fun rememberAppHaptics(appSettings: AppSettings): AppHaptics {
  val hapticFeedback = LocalHapticFeedback.current
  val isMasterEnabled by appSettings.hapticsEnabledFlow.collectAsState(true)
  val isPullToRefreshEnabled by appSettings.hapticsPullToRefreshEnabledFlow.collectAsState(true)
  val isToggleEnabled by appSettings.hapticsToggleEnabledFlow.collectAsState(true)
  val isMenuEnabled by appSettings.hapticsMenuEnabledFlow.collectAsState(true)

  return remember(hapticFeedback, isMasterEnabled, isPullToRefreshEnabled, isToggleEnabled, isMenuEnabled) {
    AppHaptics(
      hapticFeedback = hapticFeedback,
      isMasterEnabled = isMasterEnabled,
      isPullToRefreshEnabled = isPullToRefreshEnabled,
      isToggleEnabled = isToggleEnabled,
      isMenuEnabled = isMenuEnabled
    )
  }
}
