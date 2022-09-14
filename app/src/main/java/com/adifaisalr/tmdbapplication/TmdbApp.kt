package com.adifaisalr.tmdbapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
open class TmdbApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    open fun httpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }
}