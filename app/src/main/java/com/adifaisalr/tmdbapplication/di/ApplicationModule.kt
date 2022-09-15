package com.adifaisalr.tmdbapplication.di

import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.data.api.MyCallAdapterFactory
import com.adifaisalr.tmdbapplication.data.api.TmdbService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideGithubService(): TmdbService {
        return Retrofit.Builder()
            .baseUrl(Api.DEFAULT_BASE_URL)
            .client(Api.getDefaultClient())
            .addCallAdapterFactory(MyCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbService::class.java)
    }
}
