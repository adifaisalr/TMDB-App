package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.MovieDetailResponse
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(movieId: Int): DataHolder<MovieDetailResponse> =
        withContext(Dispatchers.IO) {
            return@withContext mediaRepository.getMovieDetail(movieId)
        }
}