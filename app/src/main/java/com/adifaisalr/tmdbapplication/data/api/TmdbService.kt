package com.adifaisalr.tmdbapplication.data.api

import com.adifaisalr.tmdbapplication.domain.model.DiscoverMovieResponse
import com.adifaisalr.tmdbapplication.domain.model.MovieDetailResponse
import com.adifaisalr.tmdbapplication.domain.model.MovieReviewsResponse
import com.adifaisalr.tmdbapplication.domain.model.PopularMovieResponse
import com.adifaisalr.tmdbapplication.domain.model.TrendingResponse
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface TmdbService {
    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrending(
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): DataHolder<TrendingResponse>

    @GET("discover/movie")
    suspend fun getDiscoverMovie(): DataHolder<DiscoverMovieResponse>

    @GET("movie/popular")
    suspend fun getPopularMovie(): DataHolder<PopularMovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int): DataHolder<MovieDetailResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(@Path("movie_id") movieId: Int): DataHolder<MovieReviewsResponse>
}
