package io.github.b4tchkn.timez.ui.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.b4tchkn.timez.data.NewsApiService
import io.github.b4tchkn.timez.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    private val newsApiService: NewsApiService,
) : ViewModel() {
    private val _articles = MutableStateFlow(mutableListOf<Article>())
    val articles: StateFlow<MutableList<Article>> = _articles

    init {
        getTopHeadlines()
    }

    private fun getTopHeadlines() =
        viewModelScope.launch {
            try {
                _articles.value = newsApiService.getTopHeadlines(country = "us").articles
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}
