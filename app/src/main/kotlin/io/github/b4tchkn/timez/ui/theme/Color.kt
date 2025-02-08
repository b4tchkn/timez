package io.github.b4tchkn.timez.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
object TimezColorTokens {
    val white = Color(0xFFFFFFFF)
    val black = Color(0xFF000000)
    val gray = Color(0xFF9E9E9E)
}

@Immutable
data class TimezColor(
    val white: Color,
    val black: Color,
    val gray: Color,
)

fun lightTimezColorScheme(): TimezColor = TimezColor(
    white = TimezColorTokens.white,
    black = TimezColorTokens.black,
    gray = TimezColorTokens.gray,
)

fun darkTimezColorScheme(): TimezColor = TimezColor(
    white = TimezColorTokens.white,
    black = TimezColorTokens.black,
    gray = TimezColorTokens.gray,
)

val LocalTimezColor = staticCompositionLocalOf {
    TimezColor(
        white = Color.Unspecified,
        black = Color.Unspecified,
        gray = Color.Unspecified,
    )
}
