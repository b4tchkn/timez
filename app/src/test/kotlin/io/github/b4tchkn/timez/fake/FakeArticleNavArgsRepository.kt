package io.github.b4tchkn.timez.fake

import io.github.b4tchkn.timez.data.repository.NavArgsRepository
import io.github.b4tchkn.timez.model.Article

class FakeArticleNavArgsRepository(
    private val shouldFail: Boolean = false,
) : NavArgsRepository<Article> {
    val data = mutableMapOf<String, Article>()

    override fun get(key: String): Article? =
        if (shouldFail) throw Exception() else data[key]

    override fun save(entry: Map.Entry<String, Article>): Article = Article.Default

    override fun remove(key: String) {
    }
}
