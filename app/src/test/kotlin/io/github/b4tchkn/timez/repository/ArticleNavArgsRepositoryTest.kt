package io.github.b4tchkn.timez.repository

import io.github.b4tchkn.timez.data.repository.ArticleNavArgsRepository
import io.github.b4tchkn.timez.datastore.MemoryDataStoreKeys
import io.github.b4tchkn.timez.fake.FakeMemoryDataStore
import io.github.b4tchkn.timez.model.Article
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.AbstractMap.SimpleEntry

class ArticleNavArgsRepositoryTest {
    @Test
    fun `get returns empty article when key does not exist`() {
        val dataStore = FakeMemoryDataStore()
        dataStore.data[MemoryDataStoreKeys.ArticleNavArgs.key] = mapOf(
            "test" to Article.Default,
        )
        val repository = ArticleNavArgsRepository(
            dataStore,
        )

        val result = repository.get("test2")

        assertEquals(null, result)
    }

    @Test
    fun `get returns correct value when key exists`() {
        val dataStore = FakeMemoryDataStore()
        dataStore.data[MemoryDataStoreKeys.ArticleNavArgs.key] = mapOf(
            "test" to Article.Default,
        )
        val repository = ArticleNavArgsRepository(dataStore)

        val result = repository.get("test")

        assertEquals(Article.Default, result)
    }

    @Test
    fun `save returns correct value when key does not exist`() {
        val dataStore = FakeMemoryDataStore()
        val repository = ArticleNavArgsRepository(dataStore)

        val entry = SimpleEntry("test", Article.Default.copy(title = "test"))
        val result = repository.save(entry)

        assertEquals(Article.Default.copy(title = "test"), result)

        val savedData = repository.get("test")
        assertEquals(Article.Default.copy(title = "test"), savedData)
    }

    @Test
    fun `remove value`() {
        val dataStore = FakeMemoryDataStore()
        dataStore.data[MemoryDataStoreKeys.ArticleNavArgs.key] = mapOf(
            "test" to Article.Default,
        )
        val repository = ArticleNavArgsRepository(dataStore)

        repository.remove("test")
        val dataAfterRemoved = repository.get("test")
        assertEquals(null, dataAfterRemoved)
    }
}
