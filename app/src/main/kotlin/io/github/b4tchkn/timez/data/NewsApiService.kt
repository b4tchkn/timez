package io.github.b4tchkn.timez.data

import io.github.b4tchkn.timez.data.network.AuthHeaderInterceptor
import io.github.b4tchkn.timez.model.News
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object NewsApi {
    val service: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
    ): News
}

private const val BASE_URL = "https://newsapi.org/v2/"

private val json =
    Json {
        ignoreUnknownKeys = true
    }

private val retrofit =
    Retrofit
        .Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(okHttpClient())
        .build()

private fun okHttpClient() =
    OkHttpClient
        .Builder()
        .addInterceptor(AuthHeaderInterceptor())
        .build()
