package com.adifaisalr.tmdbapplication.domain.repository

import com.adifaisalr.tmdbapplication.domain.model.DiscoverMedia
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.MediaReview
import com.adifaisalr.tmdbapplication.domain.model.PopularMedia
import com.adifaisalr.tmdbapplication.domain.model.SearchMedia
import com.adifaisalr.tmdbapplication.domain.model.TrendingMedia
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder

interface MediaRepository {
    suspend fun getTrendingMedias(mediaType: String, timeWindow: String): DataHolder<TrendingMedia>
    suspend fun getDiscoverMovies(): DataHolder<DiscoverMedia>
    suspend fun getDiscoverTvs(): DataHolder<DiscoverMedia>
    suspend fun getPopularMovies(): DataHolder<PopularMedia>
    suspend fun getPopularTvs(): DataHolder<PopularMedia>
    suspend fun getMovieDetail(movieId: Int): DataHolder<Media>
    suspend fun getMovieReviews(movieId: Int): DataHolder<MediaReview>
    suspend fun getTvDetail(tvId: Int): DataHolder<Media>
    suspend fun getTvReviews(tvId: Int): DataHolder<MediaReview>
    suspend fun searchMedia(keyword: String, page: Int): DataHolder<SearchMedia>
}