package com.adifaisalr.tmdbapplication

import android.app.Application
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.data.api.ApiBaseConfigurator
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
open class TmdbApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Api.setBaseConfigurator(object : ApiBaseConfigurator {
            override fun newHttpClientBuilder(): OkHttpClient.Builder = httpClientBuilder()
        })
    }

    open fun httpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }
}