package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMediaDetailUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(mediaType: MediaViewModel.Companion.MediaType, mediaId: Int): DataHolder<Media> =
        withContext(Dispatchers.IO) {
            val result =
                if (mediaType == MediaViewModel.Companion.MediaType.MOVIES) mediaRepository.getMovieDetail(mediaId)
                else mediaRepository.getTvDetail(mediaId)
            return@withContext result
        }
}