package io.github.b4tchkn.timez

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import okhttp3.internal.http2.Http2Reader.Companion.logger
import timber.log.Timber

@HiltAndroidApp
class TimezApplication :
    Application(),
    SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        initializeTimber()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader
            .Builder(context)
            .crossfade(true)
            .build()
    }
}
