package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.PopularMovieResponse
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPopularMovieUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(): DataHolder<PopularMovieResponse> =
        withContext(Dispatchers.IO) {
            return@withContext mediaRepository.getPopularMovies()
        }
}