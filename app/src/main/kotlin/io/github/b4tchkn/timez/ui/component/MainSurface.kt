package io.github.b4tchkn.timez.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.b4tchkn.timez.ui.theme.TimezTheme

@Composable
fun MainSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    TimezTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
        ) {
            content()
        }
    }
}
