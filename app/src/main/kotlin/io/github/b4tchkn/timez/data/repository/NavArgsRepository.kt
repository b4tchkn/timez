package io.github.b4tchkn.timez.data.repository

interface NavArgsRepository<T> {
    fun get(key: String): T?

    fun save(entry: Map.Entry<String, T>): T

    fun remove(key: String)
}
