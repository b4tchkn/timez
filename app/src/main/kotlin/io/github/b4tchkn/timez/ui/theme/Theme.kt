package io.github.b4tchkn.timez.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40,
        /* Other default colors to override
        background = Color(0xFFFFFBFE),
        surface = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
         */
    )

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
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
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
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}

object TimezTheme {
    val typography: TimezTypography
        @Composable
        get() = LocalTimezTypography.current
}
