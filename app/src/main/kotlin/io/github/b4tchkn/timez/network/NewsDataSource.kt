package io.github.b4tchkn.timez.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.b4tchkn.timez.model.News
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDataSource {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
    ): News
}

@Module
@InstallIn(SingletonComponent::class)
object NewsDataSourceModule {
    @Provides
    fun provide(retrofit: Retrofit): NewsDataSource = retrofit.create()
}
