package io.github.b4tchkn.timez.fake

import app.cash.turbine.Turbine
import io.github.b4tchkn.timez.data.repository.NewsRepository
import io.github.b4tchkn.timez.model.Article

class FakeNewsRepository(
    private val shouldFail: Boolean = false,
) : NewsRepository {
    val articles = Turbine<List<Article>>()

    override suspend fun topHeadlines(): List<Article> {
        if (shouldFail) {
            throw fakeNewsRepositoryException
        } else {
            return articles.awaitItem()
        }
    }
}

val fakeNewsRepositoryException = Exception()
