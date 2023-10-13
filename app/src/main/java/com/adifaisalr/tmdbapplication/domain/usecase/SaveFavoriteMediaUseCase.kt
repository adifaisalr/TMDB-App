package com.adifaisalr.tmdbapplication.domain.usecase

import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveFavoriteMediaUseCase @Inject constructor(private val repository: MediaRepository) {

    suspend operator fun invoke(media: Media): Long = withContext(Dispatchers.IO) {
        return@withContext repository.insertMedia(media)
    }
}