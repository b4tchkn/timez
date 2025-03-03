package io.github.b4tchkn.timez.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideOkHttpClient(
        authHeaderInterceptor: AuthHeaderInterceptor,
    ): OkHttpClient = OkHttpClient.Builder().addNetworkInterceptor(authHeaderInterceptor).build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit = Retrofit
        .Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://newsapi.org/v2/")
        .client(okHttpClient)
        .build()

    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }
}
