package io.github.b4tchkn.timez.ui.component

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberAppSnackbarState(): AppSnackbarState {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarState = remember { AppSnackbarState(coroutineScope, snackbarHostState) }

    return snackbarState
}

@Stable
class AppSnackbarState(
    private val coroutineScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState,
) {
    fun showSnackbar(text: String) = coroutineScope.launch {
        snackbarHostState.showSnackbar(text)
    }
}
