package com.adifaisalr.tmdbapplication.api

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object Api {
    private lateinit var baseConfigurator: ApiBaseConfigurator

    fun setBaseConfigurator(configurator: ApiBaseConfigurator) {
        baseConfigurator = configurator
    }

    fun newHttpClientBuilder(): OkHttpClient.Builder {
        return baseConfigurator.newHttpClientBuilder()
    }

    const val DEFAULT_BASE_URL = "https://api.github.com/"
    const val TIMEOUT = 60000

    @Synchronized
    fun getDefaultClient(): OkHttpClient {
        return newHttpClientBuilder()
            .connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .build()
    }
}