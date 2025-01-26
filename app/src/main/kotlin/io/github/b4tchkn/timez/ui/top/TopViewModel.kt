package io.github.b4tchkn.timez.ui.top

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.b4tchkn.timez.domain.newsapi.GetTopHeadlinesUseCase
import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.ui.foundation.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
) : MoleculeViewModel<TopUiEvent, TopUiModel>() {
    private val _articles = MutableStateFlow(mutableListOf<Article>())
    val articles: StateFlow<MutableList<Article>> = _articles

    init {
        getTopHeadlines()
    }

    private fun getTopHeadlines() =
        viewModelScope.launch {
            getTopHeadlinesUseCase
                .invoke(Unit)
                .onSuccess { _articles.value = it }
                .onFailure { it.printStackTrace() }
        }

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
                .onFailure { error = it }
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
            content = TopUiModel.Content.Default(articles = articles),
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
    }
}

sealed interface TopUiEvent {
    data object Refresh : TopUiEvent
}
