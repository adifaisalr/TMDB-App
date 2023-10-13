package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDiscoverMediaUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(mediaType: MediaViewModel.Companion.MediaType): DataHolder<HomeSectionMedia> =
        withContext(Dispatchers.IO) {
            val result =
                if (mediaType == MediaViewModel.Companion.MediaType.MOVIES) mediaRepository.getDiscoverMovies()
                else mediaRepository.getDiscoverTvs()
            return@withContext result
        }
}