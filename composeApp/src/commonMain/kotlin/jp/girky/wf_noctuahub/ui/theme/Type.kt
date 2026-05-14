package jp.girky.wf_noctuahub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontVariation
import org.jetbrains.compose.resources.Font
import noctuahub.composeapp.generated.resources.Res
import noctuahub.composeapp.generated.resources.google_sans_flex
import noctuahub.composeapp.generated.resources.noto_sans_jp

@Composable
fun getAppFontFamily(): FontFamily {
    return FontFamily(
        // Normal
        Font(
            resource = Res.font.google_sans_flex,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Normal.weight),
                FontVariation.Setting("ROND", 100f) // カスタム丸み
            )
        ),
        Font(
            resource = Res.font.noto_sans_jp,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Normal.weight)
            )
        ),
        
        // Medium
        Font(
            resource = Res.font.google_sans_flex,
            weight = FontWeight.Medium,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Medium.weight),
                FontVariation.Setting("ROND", 100f)
            )
        ),
        Font(
            resource = Res.font.noto_sans_jp,
            weight = FontWeight.Medium,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Medium.weight)
            )
        ),
        
        // Bold
        Font(
            resource = Res.font.google_sans_flex,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Bold.weight),
                FontVariation.Setting("ROND", 100f)
            )
        ),
        Font(
            resource = Res.font.noto_sans_jp,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Bold.weight)
            )
        )
    )
}

@Composable
fun getAppFontFamilyCondensed(): FontFamily {
    return FontFamily(
        // Normal
        Font(
            resource = Res.font.google_sans_flex,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Normal.weight),
                FontVariation.Setting("ROND", 100f),
                FontVariation.Setting("wdth", 85f)
            )
        ),
        Font(
            resource = Res.font.noto_sans_jp,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Normal.weight),
                FontVariation.Setting("wdth", 85f)
            )
        ),
        
        // Medium
        Font(
            resource = Res.font.google_sans_flex,
            weight = FontWeight.Medium,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Medium.weight),
                FontVariation.Setting("ROND", 100f),
                FontVariation.Setting("wdth", 85f)
            )
        ),
        Font(
            resource = Res.font.noto_sans_jp,
            weight = FontWeight.Medium,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Medium.weight),
                FontVariation.Setting("wdth", 85f)
            )
        ),
        
        // Bold
        Font(
            resource = Res.font.google_sans_flex,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Bold.weight),
                FontVariation.Setting("ROND", 100f),
                FontVariation.Setting("wdth", 85f)
            )
        ),
        Font(
            resource = Res.font.noto_sans_jp,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Bold.weight),
                FontVariation.Setting("wdth", 85f)
            )
        )
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

@Composable
fun getAppTypographyCondensed(): Typography {
    val defaultTypography = Typography()
    val appFontFamilyCondensed = getAppFontFamilyCondensed()
    
    return Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = appFontFamilyCondensed),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = appFontFamilyCondensed),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = appFontFamilyCondensed),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = appFontFamilyCondensed),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = appFontFamilyCondensed),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = appFontFamilyCondensed),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = appFontFamilyCondensed),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = appFontFamilyCondensed),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = appFontFamilyCondensed),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = appFontFamilyCondensed),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = appFontFamilyCondensed),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = appFontFamilyCondensed),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = appFontFamilyCondensed),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = appFontFamilyCondensed),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = appFontFamilyCondensed),
    )
}
