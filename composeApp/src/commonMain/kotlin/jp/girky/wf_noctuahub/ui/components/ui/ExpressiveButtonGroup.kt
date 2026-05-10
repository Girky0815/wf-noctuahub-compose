package jp.girky.wf_noctuahub.ui.components.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

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
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween, Alignment.CenterHorizontally)
    ) {
        options.forEachIndexed { index, option ->
            ToggleButton(
                checked = selectedIndex == index,
                onCheckedChange = { option.onClick() },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                }
            ) {
                option.icon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(ToggleButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ToggleButtonDefaults.IconSpacing))
                }
                Text(option.label)
            }
        }
    }
}
