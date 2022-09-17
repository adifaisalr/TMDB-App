package com.adifaisalr.tmdbapplication.domain.repository

import com.adifaisalr.tmdbapplication.domain.model.DiscoverMovieResponse
import com.adifaisalr.tmdbapplication.domain.model.MovieDetailResponse
import com.adifaisalr.tmdbapplication.domain.model.MovieReviewsResponse
import com.adifaisalr.tmdbapplication.domain.model.PopularMovieResponse
import com.adifaisalr.tmdbapplication.domain.model.TrendingResponse
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder

interface MediaRepository {
    suspend fun getTrendingMedias(mediaType: String, timeWindow: String): DataHolder<TrendingResponse>
    suspend fun getDiscoverMovies(): DataHolder<DiscoverMovieResponse>
    suspend fun getPopularMovies(): DataHolder<PopularMovieResponse>
    suspend fun getMovieDetail(movieId: Int): DataHolder<MovieDetailResponse>
    suspend fun getMovieReviews(movieId: Int): DataHolder<MovieReviewsResponse>
}