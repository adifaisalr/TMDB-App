package com.adifaisalr.tmdbapplication.libs.domain.usecase

import com.adifaisalr.tmdbapplication.libs.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.libs.domain.model.MediaType
import com.adifaisalr.tmdbapplication.libs.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.libs.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPopularMediaUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(
        mediaType: MediaType
    ): DataHolder<HomeSectionMedia> =
        withContext(Dispatchers.IO) {
            val result =
                if (mediaType == MediaType.MOVIES) mediaRepository.getPopularMovies()
                else mediaRepository.getPopularTvs()
            return@withContext result
        }
}