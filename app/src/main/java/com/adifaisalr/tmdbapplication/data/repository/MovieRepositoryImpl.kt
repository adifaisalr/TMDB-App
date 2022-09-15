package com.adifaisalr.tmdbapplication.data.repository

import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.data.api.TmdbService
import com.adifaisalr.tmdbapplication.domain.model.DiscoverMovieResponse
import com.adifaisalr.tmdbapplication.domain.repository.MovieRepository
import com.adifaisalr.tmdbapplication.domain.model.TrendingResponse

class MovieRepositoryImpl(
    private val tmdbService: TmdbService
) : MovieRepository {

    override suspend fun getTrendingMedias(mediaType: String, timeWindow: String): DataHolder<TrendingResponse> {
        return tmdbService.getTrending(mediaType, timeWindow)
    }

    override suspend fun getDiscoverMovies(): DataHolder<DiscoverMovieResponse> {
        return tmdbService.getDiscoverMovie()
    }
}