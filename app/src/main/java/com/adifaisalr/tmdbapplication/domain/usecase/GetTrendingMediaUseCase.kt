package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.TrendingResponse
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTrendingMediaUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(mediaType: String, timeWindow: String): DataHolder<TrendingResponse> =
        withContext(Dispatchers.IO) {
            return@withContext movieRepository.getTrendingMedias(mediaType, timeWindow)
        }
}