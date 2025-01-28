package io.github.b4tchkn.timez.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.b4tchkn.timez.ui.theme.TimezTheme

@Composable
fun MainSurface(
    content: @Composable () -> Unit,
) {
    TimezTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            content()
        }
    }
}
