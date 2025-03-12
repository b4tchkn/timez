package io.github.b4tchkn.timez.datastore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.b4tchkn.timez.datastore.AppDataStore
import io.github.b4tchkn.timez.datastore.MemoryDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Singleton
    @Binds
    abstract fun bindMemoryDataStore(memoryDataStore: MemoryDataStore): AppDataStore
}
