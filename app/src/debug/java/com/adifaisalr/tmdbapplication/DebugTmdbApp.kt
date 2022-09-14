package com.adifaisalr.tmdbapplication

import com.facebook.soloader.SoLoader
import okhttp3.OkHttpClient
import timber.log.Timber
import timber.log.Timber.DebugTree

class DebugTmdbApp : TmdbApp() {

    override fun onCreate() {
        SoLoader.init(this, false)
        Timber.plant(DebugTree())
        FlipperWrapper.setup(this)
        super.onCreate()
    }

    override fun httpClientBuilder(): OkHttpClient.Builder {
        return super.httpClientBuilder()
            .addNetworkInterceptor(FlipperWrapper.flipperNetworkInterceptor)
    }
}