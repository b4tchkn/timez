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
import io.github.b4tchkn.timez.feature.navArgs
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailUiModel.Content.Default
import io.github.b4tchkn.timez.feature.newsdetail.NewsDetailUiModel.Content.Empty
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.foundation.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : MoleculeViewModel<NewsDetailUiEvent, NewsDetailUiModel>() {
    @Composable
    override fun state(events: Flow<NewsDetailUiEvent>): NewsDetailUiModel {
        var loading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<Throwable?>(null) }
        var article by remember { mutableStateOf<Article?>(null) }

        val scope = rememberCoroutineScope()

        fun refresh() = scope.launch(loading = { loading = it }) {
            runCatching {
                savedStateHandle.navArgs<NewsDetailScreenNavArgs>().article
            }.onSuccess {
                article = it
            }.onFailure {
                error = it
                error?.printStackTrace()
            }
        }

        LaunchedEffect(Unit) { refresh() }

        return NewsDetailUiModel(
            loading = loading,
            error = error,
            content = article?.let {
                Default(article = it)
            } ?: Empty,
        )
    }
}

data class NewsDetailUiModel(
    val loading: Boolean,
    val error: Throwable?,
    val content: Content,
) {
    sealed interface Content {
        data class Default(
            val article: Article,
        ) : Content

        data object Empty : Content
    }
}

sealed interface NewsDetailUiEvent {
    data object Refresh : NewsDetailUiEvent
}
