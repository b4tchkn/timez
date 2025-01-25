package io.github.b4tchkn.timez.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
)
