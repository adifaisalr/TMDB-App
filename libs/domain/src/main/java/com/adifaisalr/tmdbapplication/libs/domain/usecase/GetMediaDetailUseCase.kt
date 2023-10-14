package com.adifaisalr.tmdbapplication.libs.domain.usecase

import com.adifaisalr.tmdbapplication.libs.domain.model.Media
import com.adifaisalr.tmdbapplication.libs.domain.model.MediaType
import com.adifaisalr.tmdbapplication.libs.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.libs.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMediaDetailUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(mediaType: MediaType, mediaId: Int): DataHolder<Media> =
        withContext(Dispatchers.IO) {
            val result =
                if (mediaType == MediaType.MOVIES) mediaRepository.getMovieDetail(mediaId)
                else mediaRepository.getTvDetail(mediaId)
            return@withContext result
        }
}