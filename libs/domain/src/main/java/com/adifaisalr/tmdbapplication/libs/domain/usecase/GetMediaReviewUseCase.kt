package com.adifaisalr.tmdbapplication.libs.domain.usecase

import com.adifaisalr.tmdbapplication.libs.domain.model.MediaReview
import com.adifaisalr.tmdbapplication.libs.domain.model.MediaType
import com.adifaisalr.tmdbapplication.libs.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.libs.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMediaReviewUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(
        mediaType: MediaType,
        mediaId: Int
    ): DataHolder<MediaReview> =
        withContext(Dispatchers.IO) {
            val result =
                if (mediaType == MediaType.MOVIES) mediaRepository.getMovieReviews(mediaId)
                else mediaRepository.getTvReviews(mediaId)
            return@withContext result
        }
}