package io.github.b4tchkn.timez.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun <S : Any, D : Any> MoleculeViewModel<D, *>.LaunchStateEffect(
    state: S?,
    doneEvent: D,
    effect: suspend CoroutineScope.(S) -> Unit,
) {
    LaunchedEffect(state) {
        state ?: return@LaunchedEffect
        effect(state)
        take(doneEvent)
    }
}
