package io.github.b4tchkn.timez.feature.top

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.b4tchkn.timez.domain.newsapi.GetTopHeadlinesUseCase
import io.github.b4tchkn.timez.feature.top.TopUiModel.Content.Default
import io.github.b4tchkn.timez.feature.top.TopUiModel.Content.Empty
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.foundation.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
) : MoleculeViewModel<TopUiEvent, TopUiModel>() {
    @Composable
    override fun state(events: Flow<TopUiEvent>): TopUiModel {
        var loading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<Throwable?>(null) }
        var articles by remember { mutableStateOf<List<Article>>(emptyList()) }

        val scope = rememberCoroutineScope()

        fun refresh() = scope.launch(loading = { loading = it }) {
            getTopHeadlinesUseCase
                .invoke(Unit)
                .onSuccess { articles = it }
                .onFailure {
                    error = it
                    error?.printStackTrace()
                }
        }

        LaunchedEffect(Unit) { refresh() }

        LaunchedEffect(Unit) {
            events.collect {
                when (it) {
                    TopUiEvent.Refresh -> refresh()
                }
            }
        }

        return TopUiModel(
            loading = loading,
            error = error,
            content = if (articles.isEmpty()) Empty else Default(articles = articles),
        )
    }
}

data class TopUiModel(
    val loading: Boolean,
    val error: Throwable?,
    val content: Content,
) {
    sealed interface Content {
        data class Default(
            val articles: List<Article>,
        ) : Content

        data object Empty : Content
    }
}

sealed interface TopUiEvent {
    data object Refresh : TopUiEvent
}
