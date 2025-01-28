package io.github.b4tchkn.timez.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import io.github.b4tchkn.timez.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class MoleculeViewModel<Event, State> : ViewModel() {
    private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    // Events have a capacity large enough to handle simultaneous UI events, but
    // small enough to surface issues if they get backed up for some reason.
    // Set a small replay value. So events sent before the flow is collected can be handled.
    private val events = MutableSharedFlow<Event>(
        replay = 10,
        extraBufferCapacity = 20,
    )

    val state: StateFlow<State> by lazy(LazyThreadSafetyMode.NONE) {
        scope.launchMolecule(mode = RecompositionMode.ContextClock) {
            state(events)
        }
    }

    fun take(event: Event) {
        if (!events.tryEmit(event)) {
            if (BuildConfig.DEBUG) {
                error("Event buffer overflow: $event.")
            } else {
                Timber.w("Event buffer overflow: $event.")
            }
        }
    }

    @Composable
    protected abstract fun state(events: Flow<Event>): State

    protected fun CoroutineScope.launch(
        context: CoroutineContext = EmptyCoroutineContext,
        loading: (Boolean) -> Unit,
        block: suspend CoroutineScope.() -> Unit,
    ) = launch(context) {
        try {
            loading(true)
            block()
        } finally {
            loading(false)
        }
    }
}
