package io.github.b4tchkn.timez.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
object TimezTypeScaleTokens {
    val h12 = 12.sp
    val h14 = 14.sp
    val h16 = 16.sp
    val h18 = 18.sp
    val h20 = 20.sp
    val h24 = 24.sp
    val h32 = 32.sp
    val h36 = 36.sp
    val h40 = 40.sp
}

@Immutable
object TimezTypographyTokens {
    val h12 = TextStyle(
        fontSize = TimezTypeScaleTokens.h12,
    )
    val h14 = TextStyle(
        fontSize = TimezTypeScaleTokens.h14,
    )
    val h16 = TextStyle(
        fontSize = TimezTypeScaleTokens.h16,
    )
    val h18 = TextStyle(
        fontSize = TimezTypeScaleTokens.h18,
    )
    val h20 = TextStyle(
        fontSize = TimezTypeScaleTokens.h20,
    )
    val h24 = TextStyle(
        fontSize = TimezTypeScaleTokens.h24,
    )
    val h32 = TextStyle(
        fontSize = TimezTypeScaleTokens.h32,
    )
    val h36 = TextStyle(
        fontSize = TimezTypeScaleTokens.h36,
    )
    val h40 = TextStyle(
        fontSize = TimezTypeScaleTokens.h40,
    )
}

@Immutable
data class TimezTypography(
    val h12: TextStyle,
    val h14: TextStyle,
    val h16: TextStyle,
    val h18: TextStyle,
    val h20: TextStyle,
    val h24: TextStyle,
    val h32: TextStyle,
    val h36: TextStyle,
    val h40: TextStyle,
)

val LocalTimezTypography = staticCompositionLocalOf {
    TimezTypography(
        h12 = TextStyle.Default,
        h14 = TextStyle.Default,
        h16 = TextStyle.Default,
        h18 = TextStyle.Default,
        h20 = TextStyle.Default,
        h24 = TextStyle.Default,
        h32 = TextStyle.Default,
        h36 = TextStyle.Default,
        h40 = TextStyle.Default,
    )
}

val DefaultTypography = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
)
