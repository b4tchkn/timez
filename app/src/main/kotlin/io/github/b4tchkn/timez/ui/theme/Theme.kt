package io.github.b4tchkn.timez.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun TimezTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                // TODO: adapt dynamic color
                if (darkTheme) darkTimezColorScheme() else lightTimezColorScheme()
            }

            darkTheme -> darkTimezColorScheme()
            else -> lightTimezColorScheme()
        }

    val typography = TimezTypography(
        h12 = DefaultTypography.merge(TimezTypographyTokens.h12),
        h14 = DefaultTypography.merge(TimezTypographyTokens.h14),
        h16 = DefaultTypography.merge(TimezTypographyTokens.h16),
        h18 = DefaultTypography.merge(TimezTypographyTokens.h18),
        h20 = DefaultTypography.merge(TimezTypographyTokens.h20),
        h24 = DefaultTypography.merge(TimezTypographyTokens.h24),
        h32 = DefaultTypography.merge(TimezTypographyTokens.h32),
        h36 = DefaultTypography.merge(TimezTypographyTokens.h36),
        h40 = DefaultTypography.merge(TimezTypographyTokens.h40),
    )

    CompositionLocalProvider(
        LocalTimezTypography provides typography,
        LocalTimezColor provides colorScheme,
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme)
                darkColorScheme()
            else
                lightColorScheme(),
            content = content,
        )
    }
}

object TimezTheme {
    val typography: TimezTypography
        @Composable
        get() = LocalTimezTypography.current

    val color: TimezColor
        @Composable
        get() = LocalTimezColor.current
}
