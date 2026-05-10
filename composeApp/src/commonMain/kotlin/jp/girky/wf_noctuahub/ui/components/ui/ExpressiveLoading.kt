package jp.girky.wf_noctuahub.ui.components.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CenteredLoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressivePullToRefreshIndicator(
    isRefreshing: Boolean,
    state: PullToRefreshState,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isRefreshing || state.distanceFraction > 0f,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.graphicsLayer {
                val scale = if (isRefreshing) 1f else state.distanceFraction.coerceIn(0f, 1f)
                scaleX = scale
                scaleY = scale
            }
        ) {
            Surface(
                shape = CircleShape,
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                LoadingIndicator(
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
