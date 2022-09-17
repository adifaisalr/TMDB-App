package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.DiscoverMovieResponse
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDiscoverMovieUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(): DataHolder<DiscoverMovieResponse> =
        withContext(Dispatchers.IO) {
            return@withContext mediaRepository.getDiscoverMovies()
        }
}