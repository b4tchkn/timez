package io.github.b4tchkn.timez.data.repository

import io.github.b4tchkn.timez.model.Article

interface NewsRepository {
    suspend fun topHeadlines(): List<Article>
}
