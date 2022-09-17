package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.MediaReview
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMediaReviewUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(
        mediaType: MediaViewModel.Companion.MediaType,
        mediaId: Int
    ): DataHolder<MediaReview> =
        withContext(Dispatchers.IO) {
            val result =
                if (mediaType == MediaViewModel.Companion.MediaType.MOVIES) mediaRepository.getMovieReviews(mediaId)
                else mediaRepository.getTvReviews(mediaId)
            return@withContext result
        }
}