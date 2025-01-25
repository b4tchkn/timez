package io.github.b4tchkn.timez.ui.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.b4tchkn.timez.data.NewsApi
import io.github.b4tchkn.timez.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TopViewModel : ViewModel() {
    private val _articles = MutableStateFlow(mutableListOf<Article>())
    val articles: StateFlow<MutableList<Article>> = _articles

    init {
        getTopHeadlines()
    }

    private fun getTopHeadlines() =
        viewModelScope.launch {
            try {
                _articles.value = NewsApi.service.getTopHeadlines(country = "us").articles
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}
