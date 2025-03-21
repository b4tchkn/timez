package io.github.b4tchkn.timez.data.repository

import io.github.b4tchkn.timez.model.Article
import kotlinx.collections.immutable.ImmutableList

interface NewsRepository {
    suspend fun topHeadlines(): ImmutableList<Article>
}
