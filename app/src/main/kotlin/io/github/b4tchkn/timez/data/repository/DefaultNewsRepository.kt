package io.github.b4tchkn.timez.data.repository

import io.github.b4tchkn.timez.model.Article
import io.github.b4tchkn.timez.network.NewsDataSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class DefaultNewsRepository @Inject constructor(
    private val newsDataSource: NewsDataSource,
) : NewsRepository {
    override suspend fun topHeadlines(): ImmutableList<Article> {
        val headlines = newsDataSource.getTopHeadlines(country = "us").articles
        return headlines.toImmutableList()
    }
}
