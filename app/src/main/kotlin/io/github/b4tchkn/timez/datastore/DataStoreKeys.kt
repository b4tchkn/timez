package io.github.b4tchkn.timez.datastore

import io.github.b4tchkn.timez.model.Article

abstract class DataStoreKey<T>(
    val key: String,
)

object MemoryDataStoreKeys {
    object ArticleNavArgs : DataStoreKey<Map<String, Article>>("article_nav_args")
}
