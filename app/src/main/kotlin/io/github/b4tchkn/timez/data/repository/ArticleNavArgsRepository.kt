package io.github.b4tchkn.timez.data.repository

import io.github.b4tchkn.timez.datastore.AppDataStore
import io.github.b4tchkn.timez.datastore.MemoryDataStoreKeys
import io.github.b4tchkn.timez.model.Article
import javax.inject.Inject

class ArticleNavArgsRepository @Inject constructor(
    private val appDataStore: AppDataStore,
) : NavArgsRepository<Article> {
    override fun get(key: String): Article? {
        val data = appDataStore.get(dataStoreKey)
        return data?.get(key)
    }

    override fun save(entry: Map.Entry<String, Article>): Article {
        val data = appDataStore.get(dataStoreKey)?.toMutableMap() ?: mutableMapOf()
        data[entry.key] = entry.value
        appDataStore.save(dataStoreKey, data)
        return data[entry.key]!!
    }

    override fun remove(key: String) {
        val data = appDataStore.get(dataStoreKey)?.toMutableMap() ?: mutableMapOf()
        data.remove(key)
        appDataStore.save(dataStoreKey, data)
    }

    private val dataStoreKey = MemoryDataStoreKeys.ArticleNavArgs
}
