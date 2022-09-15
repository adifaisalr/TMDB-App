package com.adifaisalr.tmdbapplication.data.api

import com.adifaisalr.tmdbapplication.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class Api {

    companion object {
        private lateinit var baseConfigurator: ApiBaseConfigurator

        fun setBaseConfigurator(configurator: ApiBaseConfigurator) {
            baseConfigurator = configurator
        }

        fun newHttpClientBuilder(): OkHttpClient.Builder {
            return baseConfigurator.newHttpClientBuilder()
        }

        const val DEFAULT_BASE_URL = "https://api.themoviedb.org/3/"
        const val DEFAULT_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
        const val IMAGE_SIZE_W92 = "w92/"
        const val TIMEOUT = 30000

        val defaultInterceptor = Interceptor { chain ->
            val request = chain.request()
            Timber.d("interceptor : defaultInterceptor")
            try {
                val originalHttpUrl: HttpUrl = request.url
                val url: HttpUrl = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY_V3)
                    .build()
                val requestBuilder = request.newBuilder().url(url)
                chain.proceed(requestBuilder.build())
            } catch (e: Exception) {
                e.printStackTrace()
                var msg = ""
                when (e) {
                    is SocketTimeoutException -> {
                        msg = "Timeout - Please check your internet connection"
                    }
                    is UnknownHostException -> {
                        msg = "Unable to make a connection. Please check your internet"
                    }
                    is ConnectionShutdownException -> {
                        msg = "Connection shutdown. Please check your internet"
                    }
                    is IOException -> {
                        msg = "Server is unreachable, please try again later."
                    }
                    is IllegalStateException -> {
                        msg = "${e.message}"
                    }
                    else -> {
                        msg = "${e.message}"
                    }
                }
                Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(999)
                    .message(msg)
                    .body(ResponseBody.create(null, "{${e}}")).build()
            }
        }

        @Synchronized
        fun getDefaultClient(): OkHttpClient {
            return newHttpClientBuilder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .addInterceptor(defaultInterceptor)
                .build()
        }
    }
}