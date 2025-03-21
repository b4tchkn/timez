package io.github.b4tchkn.timez.fake

import app.cash.turbine.Turbine
import io.github.b4tchkn.timez.data.repository.NewsRepository
import io.github.b4tchkn.timez.model.Article
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class FakeNewsRepository(
    private val shouldFail: Boolean = false,
) : NewsRepository {
    val articles = Turbine<List<Article>>()

    override suspend fun topHeadlines(): ImmutableList<Article> {
        if (shouldFail) {
            throw fakeNewsRepositoryException
        } else {
            return articles.awaitItem().toImmutableList()
        }
    }
}

val fakeNewsRepositoryException = Exception()
