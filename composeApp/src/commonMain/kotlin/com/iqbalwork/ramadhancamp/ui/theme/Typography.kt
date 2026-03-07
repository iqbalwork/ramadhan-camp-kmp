package com.iqbalwork.ramadhancamp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import ramadhancamp.composeapp.generated.resources.Res
import ramadhancamp.composeapp.generated.resources.lpmq_arabic
import ramadhancamp.composeapp.generated.resources.manrope_bold
import ramadhancamp.composeapp.generated.resources.manrope_medium
import ramadhancamp.composeapp.generated.resources.manrope_regular
import ramadhancamp.composeapp.generated.resources.manrope_semi_bold

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
val ManropeFont
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.manrope_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.manrope_medium,
            weight = FontWeight.Medium,
        ),
        Font(
            resource = Res.font.manrope_semi_bold,
            weight = FontWeight.SemiBold
        ),
        Font(
            resource = Res.font.manrope_bold,
            weight = FontWeight.Bold
        )
    )

val ArabicFont
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.lpmq_arabic,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )

val ArabicTypography
    @Composable get() = Typography(
        bodyLarge = TextStyle(
            fontFamily = ArabicFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = ArabicFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = ArabicFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
        titleLarge = TextStyle(
            fontFamily = ArabicFont,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        titleMedium = TextStyle(
            fontFamily = ArabicFont,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        titleSmall = TextStyle(
            fontFamily = ArabicFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
    )

val RamadhanTypography
    @Composable get() = Typography(
        bodyLarge = TextStyle(
            fontFamily = ManropeFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = ManropeFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = ManropeFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
        titleLarge = TextStyle(
            fontFamily = ManropeFont,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        titleMedium = TextStyle(
            fontFamily = ManropeFont,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        titleSmall = TextStyle(
            fontFamily = ManropeFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
    )
