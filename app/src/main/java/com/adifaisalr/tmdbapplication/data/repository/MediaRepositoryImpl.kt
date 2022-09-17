package com.adifaisalr.tmdbapplication.data.repository

import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.data.api.TmdbService
import com.adifaisalr.tmdbapplication.domain.model.DiscoverMovieResponse
import com.adifaisalr.tmdbapplication.domain.model.MovieDetailResponse
import com.adifaisalr.tmdbapplication.domain.model.MovieReviewsResponse
import com.adifaisalr.tmdbapplication.domain.model.PopularMovieResponse
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import com.adifaisalr.tmdbapplication.domain.model.TrendingResponse

class MediaRepositoryImpl(
    private val tmdbService: TmdbService
) : MediaRepository {

    override suspend fun getTrendingMedias(mediaType: String, timeWindow: String): DataHolder<TrendingResponse> {
        return tmdbService.getTrending(mediaType, timeWindow)
    }

    override suspend fun getDiscoverMovies(): DataHolder<DiscoverMovieResponse> {
        return tmdbService.getDiscoverMovie()
    }

    override suspend fun getPopularMovies(): DataHolder<PopularMovieResponse> {
        return tmdbService.getPopularMovie()
    }

    override suspend fun getMovieDetail(movieId: Int): DataHolder<MovieDetailResponse> {
        return tmdbService.getMovieDetail(movieId)
    }

    override suspend fun getMovieReviews(movieId: Int): DataHolder<MovieReviewsResponse> {
        return tmdbService.getMovieReviews(movieId)
    }
}