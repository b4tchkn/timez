package io.github.b4tchkn.timez

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import io.github.b4tchkn.timez.core.DefaultNowLocalDateTime
import io.github.b4tchkn.timez.core.LocalNowLocalDateTime
import io.github.b4tchkn.timez.feature.NavGraphs
import io.github.b4tchkn.timez.feature.destinations.TopScreenDestination
import io.github.b4tchkn.timez.ui.theme.TimezTheme

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
                    startRoute = TopScreenDestination,
                )
            }
        }
    }
}
