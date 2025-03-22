package io.github.b4tchkn.timez.feature.newsdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.b4tchkn.timez.core.onFailureIgnoreCancellation
import io.github.b4tchkn.timez.data.repository.NavArgsRepository
import io.github.b4tchkn.timez.feature.navArgs
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailUiModel.Content.Default
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailUiModel.Content.Empty
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailUiModel.MessageState
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.foundation.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val articleNavArgsRepository: NavArgsRepository<Article>,
) : MoleculeViewModel<NewsDetailUiEvent, NewsDetailUiModel>() {
    @Composable
    override fun state(events: Flow<NewsDetailUiEvent>): NewsDetailUiModel = presenter(events)

    @Composable
    fun presenter(events: Flow<NewsDetailUiEvent>): NewsDetailUiModel {
        var loading by remember { mutableStateOf(false) }
        var article by remember { mutableStateOf<Article?>(null) }
        var message by remember { mutableStateOf<MessageState?>(null) }

        val scope = rememberCoroutineScope()

        fun refresh() = scope.launch(loading = { loading = it }) {
            runCatching {
                val articleId = savedStateHandle.navArgs<NewsDetailScreenNavArgs>().articleId
                articleNavArgsRepository.get(articleId)
            }.onSuccess {
                article = it
            }.onFailureIgnoreCancellation {
                message = MessageState.Error
                it.printStackTrace()
            }
        }

        LaunchedEffect(Unit) { refresh() }

        LaunchedEffect(Unit) {
            events.collect {
                when (it) {
                    NewsDetailUiEvent.ClearMessage -> message = null
                    NewsDetailUiEvent.Pop -> {
                        val articleId = savedStateHandle.navArgs<NewsDetailScreenNavArgs>().articleId
                        articleNavArgsRepository.remove(articleId)
                        message = MessageState.NavigatePop
                    }

                    is NewsDetailUiEvent.ClickReadMore -> message = MessageState.NavigateArticle(it.url)
                }
            }
        }

        return NewsDetailUiModel(
            loading = loading,
            content = article?.let {
                Default(article = it)
            } ?: Empty,
            message = message,
        )
    }
}

data class NewsDetailUiModel(
    val loading: Boolean,
    val content: Content,
    val message: MessageState?,
) {
    sealed interface Content {
        data class Default(
            val article: Article,
        ) : Content

        data object Empty : Content
    }

    sealed interface MessageState {
        data object Error : MessageState

        data class NavigateArticle(
            val url: String,
        ) : MessageState

        data object NavigatePop : MessageState
    }
}

sealed interface NewsDetailUiEvent {
    data object ClearMessage : NewsDetailUiEvent

    data class ClickReadMore(
        val url: String,
    ) : NewsDetailUiEvent

    data object Pop : NewsDetailUiEvent
}
