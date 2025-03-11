package io.github.b4tchkn.timez.feature.top

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.b4tchkn.timez.data.repository.NewsRepository
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.foundation.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : MoleculeViewModel<TopUiEvent, TopUiModel>() {
    @Composable
    override fun state(events: Flow<TopUiEvent>): TopUiModel = presenter(events)

    @Composable
    fun presenter(events: Flow<TopUiEvent>): TopUiModel {
        var loading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<Throwable?>(null) }
        var articles by remember { mutableStateOf<List<Article>?>(null) }
        var message by remember { mutableStateOf<TopUiModel.MessageState?>(null) }

        val scope = rememberCoroutineScope()

        fun refresh() = scope.launch(loading = { loading = it }) {
            runCatching { newsRepository.topHeadlines() }
                .onSuccess { articles = it }
                .onFailure {
                    error = it
                    message = TopUiModel.MessageState.Error
                    it.printStackTrace()
                }
        }

        fun content(): TopUiModel.Content? {
            if (error != null) {
                return TopUiModel.Content.Empty
            }

            return articles?.let {
                if (it.isNotEmpty())
                    TopUiModel.Content.Default(articles = it)
                else
                    TopUiModel.Content.Empty
            }
        }

        LaunchedEffect(Unit) { refresh() }

        LaunchedEffect(Unit) {
            events.collect {
                when (it) {
                    TopUiEvent.Refresh -> refresh()
                    TopUiEvent.ClearMessage -> message = null
                }
            }
        }

        return TopUiModel(
            loading = loading,
            content = content(),
            message = message,
        )
    }
}

data class TopUiModel(
    val loading: Boolean,
    val content: Content?,
    val message: MessageState?,
) {
    sealed interface Content {
        data class Default(
            val articles: List<Article>,
        ) : Content

        data object Empty : Content
    }

    sealed interface MessageState {
        data object Error : MessageState
    }
}

sealed interface TopUiEvent {
    data object ClearMessage : TopUiEvent

    data object Refresh : TopUiEvent
}

// TODO: Remove this when impl MemoryDataStore
val navArgsMap = mutableMapOf<String, Any>()
