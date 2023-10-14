package com.adifaisalr.tmdbapplication.libs.domain.repository

import com.adifaisalr.tmdbapplication.libs.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.libs.domain.model.Media
import com.adifaisalr.tmdbapplication.libs.domain.model.MediaReview
import com.adifaisalr.tmdbapplication.libs.domain.model.SearchMedia
import com.adifaisalr.tmdbapplication.libs.domain.model.dataholder.DataHolder

interface MediaRepository {
    suspend fun getTrendingMedias(mediaType: String, timeWindow: String): DataHolder<HomeSectionMedia>
    suspend fun getDiscoverMovies(): DataHolder<HomeSectionMedia>
    suspend fun getDiscoverTvs(): DataHolder<HomeSectionMedia>
    suspend fun getPopularMovies(): DataHolder<HomeSectionMedia>
    suspend fun getPopularTvs(): DataHolder<HomeSectionMedia>
    suspend fun getMovieDetail(movieId: Int): DataHolder<Media>
    suspend fun getMovieReviews(movieId: Int): DataHolder<MediaReview>
    suspend fun getTvDetail(tvId: Int): DataHolder<Media>
    suspend fun getTvReviews(tvId: Int): DataHolder<MediaReview>
    suspend fun searchMedia(keyword: String, page: Int): DataHolder<SearchMedia>
    suspend fun insertMedia(media: Media): Long
    suspend fun deleteMedia(media: Media): Int
    suspend fun loadAllFavoriteMedias(): List<Media>
    suspend fun loadFavoriteMediaById(id: Int): Media?
}