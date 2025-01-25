package io.github.b4tchkn.timez

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.b4tchkn.timez.ui.theme.TimezTheme
import io.github.b4tchkn.timez.ui.top.TopScreen

@Composable
fun TimezApp() {
    TimezTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            TopScreen()
        }
    }
}
