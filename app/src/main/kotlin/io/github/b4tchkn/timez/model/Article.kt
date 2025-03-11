package io.github.b4tchkn.timez.model

import io.github.b4tchkn.timez.model.core.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: String? = null,
    val source: Source?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val publishedAt: LocalDateTime?,
    val content: String?,
) {
    companion object {
        val Default = Article(
            source = null,
            author = null,
            title = null,
            description = null,
            url = null,
            urlToImage = null,
            publishedAt = null,
            content = null,
        )
    }
}

@Serializable
data class Source(
    val id: String?,
    val name: String?,
) {
    companion object {
        val Default = Source(
            id = null,
            name = null,
        )
    }
}
