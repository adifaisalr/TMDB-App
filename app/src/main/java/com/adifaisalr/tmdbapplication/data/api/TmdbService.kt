package com.adifaisalr.tmdbapplication.data.api

import com.adifaisalr.tmdbapplication.domain.model.DiscoverMovieResponse
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
}
