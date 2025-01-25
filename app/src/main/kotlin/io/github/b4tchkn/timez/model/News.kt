package io.github.b4tchkn.timez.model

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val articles: MutableList<Article>,
)
