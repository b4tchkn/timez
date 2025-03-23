package io.github.b4tchkn.timez.ui.theme

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
object TimezColorTokens {
    val white = Color(0xFFFFFFFF)
    val black = Color(0xFF000000)
    val gray = Color(0xFFDCE7C8)
    val limeGreen = Color(0xFFCDEDA3)
}

@Immutable
data class TimezColor(
    val white: Color,
    val black: Color,
    val gray: Color,
    val textColor: Color,
    val primaryContainer: Color,
)

fun lightTimezColorScheme(): TimezColor = TimezColor(
    white = TimezColorTokens.white,
    black = TimezColorTokens.black,
    gray = TimezColorTokens.gray,
    textColor = TimezColorTokens.black,
    primaryContainer = TimezColorTokens.limeGreen,
)

fun darkTimezColorScheme(): TimezColor = TimezColor(
    white = TimezColorTokens.white,
    black = TimezColorTokens.black,
    gray = TimezColorTokens.gray,
    textColor = TimezColorTokens.white,
    primaryContainer = TimezColorTokens.limeGreen,
)

@SuppressLint("ComposeCompositionLocalUsage")
val LocalTimezColor = staticCompositionLocalOf {
    TimezColor(
        white = Color.Unspecified,
        black = Color.Unspecified,
        gray = Color.Unspecified,
        textColor = Color.Unspecified,
        primaryContainer = Color.Unspecified,
    )
}
