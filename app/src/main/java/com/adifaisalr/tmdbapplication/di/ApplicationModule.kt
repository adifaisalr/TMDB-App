package com.adifaisalr.tmdbapplication.di

import android.app.Application
import androidx.room.Room
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.data.api.MyCallAdapterFactory
import com.adifaisalr.tmdbapplication.data.api.TmdbService
import com.adifaisalr.tmdbapplication.data.db.MediaDao
import com.adifaisalr.tmdbapplication.data.db.MediaDb
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

    @Singleton
    @Provides
    fun provideDb(app: Application): MediaDb {
        return Room
            .databaseBuilder(app, MediaDb::class.java, "tmdb.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMediaDao(db: MediaDb): MediaDao {
        return db.mediaDao()
    }
}
