package com.adifaisalr.tmdbapplication.di

import com.adifaisalr.tmdbapplication.data.api.TmdbService
import com.adifaisalr.tmdbapplication.data.repository.MediaRepositoryImpl
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(tmdbService: TmdbService): MediaRepository {
        return MediaRepositoryImpl(tmdbService)
    }
}