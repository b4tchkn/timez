package io.github.b4tchkn.timez.data.repository

import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.network.NewsDataSource
import javax.inject.Inject

class DefaultNewsRepository @Inject constructor(
    private val newsDataSource: NewsDataSource,
) : NewsRepository {
    override suspend fun topHeadlines(): List<Article> {
        val headlines = newsDataSource.getTopHeadlines(country = "us").articles
        return headlines
    }
}
