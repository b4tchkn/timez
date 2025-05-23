package io.github.b4tchkn.timez.feature.article

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.ramcosta.composedestinations.generated.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.b4tchkn.timez.ui.component.AppWebViewClient
import io.github.b4tchkn.timez.ui.foundation.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : MoleculeViewModel<ArticleUiEvent, ArticleUiModel>() {
    @Composable
    override fun state(events: Flow<ArticleUiEvent>): ArticleUiModel {
        val webViewClient = remember { AppWebViewClient() }
        var message by remember { mutableStateOf<ArticleUiModel.MessageState?>(null) }

        LaunchedEffect(Unit) {
            events.collect {
                message = when (it) {
                    ArticleUiEvent.ClearMessage -> null
                    ArticleUiEvent.Pop -> ArticleUiModel.MessageState.NavigatePop
                }
            }
        }

        return ArticleUiModel(
            loading = webViewClient.loading,
            content = ArticleUiModel.Content.Default(
                url = savedStateHandle.navArgs<ArticleScreenNavArgs>().url,
                client = webViewClient,
            ),
            message = message,
        )
    }
}

data class ArticleUiModel(
    val loading: Boolean,
    val content: Content,
    val message: MessageState?,
) {
    sealed interface Content {
        data class Default(
            val url: String,
            val client: AppWebViewClient,
        ) : Content
    }

    sealed interface MessageState {
        data object NavigatePop : MessageState
    }
}

sealed interface ArticleUiEvent {
    data object ClearMessage : ArticleUiEvent

    data object Pop : ArticleUiEvent
}
