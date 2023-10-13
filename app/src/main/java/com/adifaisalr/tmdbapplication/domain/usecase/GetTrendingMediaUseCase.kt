package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTrendingMediaUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(mediaType: String, timeWindow: String): DataHolder<HomeSectionMedia> =
        withContext(Dispatchers.IO) {
            return@withContext mediaRepository.getTrendingMedias(mediaType, timeWindow)
        }
}