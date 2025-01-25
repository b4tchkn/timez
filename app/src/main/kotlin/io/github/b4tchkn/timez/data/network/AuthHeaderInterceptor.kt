package io.github.b4tchkn.timez.data.network

import io.github.b4tchkn.timez.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = BuildConfig.apiKey
        val request =
            chain
                .request()
                .newBuilder()
                .apply {
                    if (apiKey.isNotEmpty()) addHeader("X-Api-Key", apiKey)
                }.build()

        return chain.proceed(request)
    }
}
