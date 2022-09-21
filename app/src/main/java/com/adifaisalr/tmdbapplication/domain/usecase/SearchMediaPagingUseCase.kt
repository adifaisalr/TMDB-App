package com.adifaisalr.tmdbapplication.domain.usecase

import androidx.paging.PagingData
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchMediaPagingUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend operator fun invoke(keyword: String): Flow<PagingData<SearchItem>> =
        withContext(Dispatchers.IO) {
            return@withContext mediaRepository.searchMediaPaging(keyword)
        }
}