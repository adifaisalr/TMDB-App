package com.adifaisalr.tmdbapplication.domain.repository

import com.adifaisalr.tmdbapplication.domain.model.DiscoverMovieResponse
import com.adifaisalr.tmdbapplication.domain.model.TrendingResponse
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder

interface MovieRepository {
    suspend fun getTrendingMedias(mediaType: String, timeWindow: String): DataHolder<TrendingResponse>
    suspend fun getDiscoverMovies(): DataHolder<DiscoverMovieResponse>
}