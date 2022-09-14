package com.adifaisalr.tmdbapplication.api

import okhttp3.OkHttpClient

interface ApiBaseConfigurator {
    fun newHttpClientBuilder(): OkHttpClient.Builder
}