package io.github.b4tchkn.timez.fake

import io.github.b4tchkn.timez.data.repository.NavArgsRepository
import io.github.b4tchkn.timez.model.Article

class FakeArticleNavArgsRepository : NavArgsRepository<Article> {
    override fun get(key: String): Article? = null

    override fun save(entry: Map.Entry<String, Article>): Article = Article.Default

    override fun remove(key: String) {
    }
}
