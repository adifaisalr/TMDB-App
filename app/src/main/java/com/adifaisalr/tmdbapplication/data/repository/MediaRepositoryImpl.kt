package com.adifaisalr.tmdbapplication.data.repository

import com.adifaisalr.tmdbapplication.data.api.TmdbService
import com.adifaisalr.tmdbapplication.domain.model.DiscoverMedia
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.MediaReview
import com.adifaisalr.tmdbapplication.domain.model.PopularMedia
import com.adifaisalr.tmdbapplication.domain.model.SearchMedia
import com.adifaisalr.tmdbapplication.domain.model.TrendingMedia
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val tmdbService: TmdbService
) : MediaRepository {

    override suspend fun getTrendingMedias(mediaType: String, timeWindow: String): DataHolder<TrendingMedia> {
        return tmdbService.getTrending(mediaType, timeWindow)
    }

    override suspend fun getDiscoverMovies(): DataHolder<DiscoverMedia> {
        return tmdbService.getDiscoverMovie()
    }

    override suspend fun getDiscoverTvs(): DataHolder<DiscoverMedia> {
        return tmdbService.getDiscoverTv()
    }

    override suspend fun getPopularMovies(): DataHolder<PopularMedia> {
        return tmdbService.getPopularMovie()
    }

    override suspend fun getPopularTvs(): DataHolder<PopularMedia> {
        return tmdbService.getPopularTv()
    }

    override suspend fun getMovieDetail(movieId: Int): DataHolder<Media> {
        return tmdbService.getMovieDetail(movieId)
    }

    override suspend fun getMovieReviews(movieId: Int): DataHolder<MediaReview> {
        return tmdbService.getMovieReviews(movieId)
    }

    override suspend fun getTvDetail(tvId: Int): DataHolder<Media> {
        return tmdbService.getTvDetail(tvId)
    }

    override suspend fun getTvReviews(tvId: Int): DataHolder<MediaReview> {
        return tmdbService.getTvReviews(tvId)
    }

    override suspend fun searchMedia(keyword: String, page: Int): DataHolder<SearchMedia> {
        return tmdbService.searchMedia(keyword, page)
    }
}