package com.adifaisalr.tmdbapplication.data.api

import com.adifaisalr.tmdbapplication.libs.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.libs.domain.model.Media
import com.adifaisalr.tmdbapplication.libs.domain.model.MediaReview
import com.adifaisalr.tmdbapplication.libs.domain.model.SearchMedia
import com.adifaisalr.tmdbapplication.libs.domain.model.dataholder.DataHolder
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API access points
 */
interface TmdbService {
    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrending(
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): DataHolder<HomeSectionMedia>

    @GET("discover/movie")
    suspend fun getDiscoverMovie(): DataHolder<HomeSectionMedia>

    @GET("discover/tv")
    suspend fun getDiscoverTv(): DataHolder<HomeSectionMedia>

    @GET("movie/popular")
    suspend fun getPopularMovie(): DataHolder<HomeSectionMedia>

    @GET("tv/popular")
    suspend fun getPopularTv(): DataHolder<HomeSectionMedia>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int): DataHolder<Media>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(@Path("movie_id") movieId: Int): DataHolder<MediaReview>

    @GET("tv/{tv_id}")
    suspend fun getTvDetail(@Path("tv_id") tvId: Int): DataHolder<Media>

    @GET("tv/{tv_id}/reviews")
    suspend fun getTvReviews(@Path("tv_id") tvId: Int): DataHolder<MediaReview>

    @GET("search/multi")
    suspend fun searchMedia(@Query("query") keyword: String, @Query("page") page: Int): DataHolder<SearchMedia>
}
