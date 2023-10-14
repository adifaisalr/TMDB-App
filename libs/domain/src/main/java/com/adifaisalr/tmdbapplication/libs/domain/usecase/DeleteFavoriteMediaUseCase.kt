package com.adifaisalr.tmdbapplication.libs.domain.usecase

import com.adifaisalr.tmdbapplication.libs.domain.model.Media
import com.adifaisalr.tmdbapplication.libs.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteFavoriteMediaUseCase @Inject constructor(private val repository: MediaRepository) {

    suspend operator fun invoke(media: Media): Int = withContext(Dispatchers.IO) {
        return@withContext repository.deleteMedia(media)
    }
}