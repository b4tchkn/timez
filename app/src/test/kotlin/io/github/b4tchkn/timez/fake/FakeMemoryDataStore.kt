package io.github.b4tchkn.timez.fake

import io.github.b4tchkn.timez.datastore.AppDataStore
import io.github.b4tchkn.timez.datastore.DataStoreKey

@Suppress("UNCHECKED_CAST")
class FakeMemoryDataStore : AppDataStore {
    val data = mutableMapOf<String, Any>()

    override fun <T> get(storeKey: DataStoreKey<T>): T? = data[storeKey.key] as? T

    override fun <T> save(storeKey: DataStoreKey<T>, value: T): T? {
        data[storeKey.key] = value as Any
        return data[storeKey.key] as? T
    }

    override fun remove(storeKey: DataStoreKey<*>) {
        data.remove(storeKey.key)
    }
}
