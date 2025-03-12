package io.github.b4tchkn.timez.datastore

import javax.inject.Inject

interface AppDataStore {
    fun <T> get(storeKey: DataStoreKey<T>): T?

    fun <T> save(storeKey: DataStoreKey<T>, value: T): T?

    fun remove(storeKey: DataStoreKey<*>)
}

class MemoryDataStore @Inject constructor() : AppDataStore {
    private val data = mutableMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(storeKey: DataStoreKey<T>): T? = data[storeKey.key] as? T

    @Suppress("UNCHECKED_CAST")
    override fun <T> save(storeKey: DataStoreKey<T>, value: T): T {
        data[storeKey.key] = value as Any

        return data[storeKey.key] as T
    }

    override fun remove(storeKey: DataStoreKey<*>) {
        data.remove(storeKey.key)
    }
}
