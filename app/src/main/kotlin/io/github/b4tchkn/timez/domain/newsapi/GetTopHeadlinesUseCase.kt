package io.github.b4tchkn.timez.domain.newsapi

import io.github.b4tchkn.timez.data.NewsApiService
import io.github.b4tchkn.timez.model.Article
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val newsApiService: NewsApiService,
) : UseCase<Unit, List<Article>> {
    override suspend fun invoke(param: Unit): Result<List<Article>> = runCatching {
        newsApiService.getTopHeadlines(country = "us").articles
    }
}
