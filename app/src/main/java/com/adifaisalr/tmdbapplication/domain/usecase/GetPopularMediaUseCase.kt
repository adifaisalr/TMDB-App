package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.PopularMedia
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPopularMediaUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(
        mediaType: MediaViewModel.Companion.MediaType
    ): DataHolder<PopularMedia> =
        withContext(Dispatchers.IO) {
            val result =
                if (mediaType == MediaViewModel.Companion.MediaType.MOVIES) mediaRepository.getPopularMovies()
                else mediaRepository.getPopularTvs()
            return@withContext result
        }
}