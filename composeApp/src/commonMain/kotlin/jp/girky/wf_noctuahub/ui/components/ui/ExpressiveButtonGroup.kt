package jp.girky.wf_noctuahub.ui.components.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class ExpressiveButtonOption(
    val label: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveButtonGroup(
    options: List<ExpressiveButtonOption>,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    ButtonGroup(modifier = modifier) {
        options.forEachIndexed { index, option ->
            val content: @Composable () -> Unit = {
                option.icon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                }
                Text(option.label)
            }
            if (index == selectedIndex) {
                Button(onClick = option.onClick, content = { content() })
            } else {
                FilledTonalButton(onClick = option.onClick, content = { content() })
            }
        }
    }
}
