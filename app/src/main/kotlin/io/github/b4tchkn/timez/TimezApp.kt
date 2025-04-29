package io.github.b4tchkn.timez

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import io.github.b4tchkn.timez.core.DefaultNowLocalDateTime
import io.github.b4tchkn.timez.core.LocalNowLocalDateTime
import io.github.b4tchkn.timez.ui.theme.TimezTheme

@SuppressLint("ComposeModifierMissing")
@Composable
fun TimezApp() {
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNowLocalDateTime provides DefaultNowLocalDateTime,
    ) {
        TimezTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                DestinationsNavHost(
                    navController = navController,
                    navGraph = NavGraphs.root,
                )
            }
        }
    }
}
