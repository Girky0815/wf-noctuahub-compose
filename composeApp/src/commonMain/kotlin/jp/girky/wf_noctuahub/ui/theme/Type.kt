package jp.girky.wf_noctuahub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import noctuahub.composeapp.generated.resources.Res
import noctuahub.composeapp.generated.resources.google_sans_flex
import noctuahub.composeapp.generated.resources.noto_sans_jp

@Composable
fun getAppFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.google_sans_flex, weight = FontWeight.Normal, style = FontStyle.Normal),
        Font(Res.font.noto_sans_jp, weight = FontWeight.Normal, style = FontStyle.Normal)
    )
}

@Composable
fun getAppTypography(): Typography {
    val defaultTypography = Typography()
    val appFontFamily = getAppFontFamily()
    
    return Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = appFontFamily),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = appFontFamily),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = appFontFamily),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = appFontFamily),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = appFontFamily),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = appFontFamily),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = appFontFamily),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = appFontFamily),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = appFontFamily),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = appFontFamily),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = appFontFamily),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = appFontFamily),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = appFontFamily),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = appFontFamily),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = appFontFamily),
    )
}
