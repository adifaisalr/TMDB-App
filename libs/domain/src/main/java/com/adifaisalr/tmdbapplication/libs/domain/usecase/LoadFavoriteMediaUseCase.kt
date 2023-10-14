package com.adifaisalr.tmdbapplication.libs.domain.usecase

import com.adifaisalr.tmdbapplication.libs.domain.model.Media
import com.adifaisalr.tmdbapplication.libs.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadFavoriteMediaUseCase @Inject constructor(private val repository: MediaRepository) {

    suspend fun loadAll(): List<Media> =
        withContext(Dispatchers.IO) {
            return@withContext repository.loadAllFavoriteMedias()
        }

    suspend fun loadById(id: Int): Media? =
        withContext(Dispatchers.IO) {
            return@withContext repository.loadFavoriteMediaById(id)
        }
}