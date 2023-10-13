package com.adifaisalr.tmdbapplication.presentation.ui.detail

import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.Review
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.usecase.DeleteFavoriteMediaUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.GetMediaDetailUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.GetMediaReviewUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.LoadFavoriteMediaUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.SaveFavoriteMediaUseCase
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    private val getMediaDetailUseCase: GetMediaDetailUseCase,
    private val getMediaReviewUseCase: GetMediaReviewUseCase,
    private val loadFavoriteMediaUseCase: LoadFavoriteMediaUseCase,
    private val saveFavoriteMediaUseCase: SaveFavoriteMediaUseCase,
    private val deleteFavoriteMediaUseCase: DeleteFavoriteMediaUseCase,
) : BaseViewModel<MediaDetailViewState, MediaDetailActionResult>(
    initialState = MediaDetailViewState()
) {

    var mediaId by Delegates.notNull<Int>()
    lateinit var mediaType: MediaViewModel.Companion.MediaType

    protected var media: Media? = null
    protected var reviewItemList: List<Review> = emptyList()
    protected var isFavorite = false

    fun getMovieDetail() = viewModelScope.launch {
        handleActionResult(MediaDetailActionResult.SetMediaLoading(true))
        when (val response = getMediaDetailUseCase(mediaType, mediaId)) {
            is DataHolder.Success -> {
                media = response.data?.apply {
                    type = mediaType.type
                }
                handleActionResult(MediaDetailActionResult.SetMedia(media))
                getMovieReviews()
                loadFavoriteMedia()
            }

            else -> {
                handleActionResult(MediaDetailActionResult.SetError("Error fetching data"))
            }
        }
    }

    private fun getMovieReviews() = viewModelScope.launch {
        handleActionResult(MediaDetailActionResult.SetReviewLoading(true))
        when (val response = getMediaReviewUseCase(mediaType, mediaId)) {
            is DataHolder.Success -> {
                reviewItemList = response.data?.reviews ?: listOf()
                handleActionResult(MediaDetailActionResult.SetReviewItemList(reviewItemList))
            }

            else -> {
                handleActionResult(MediaDetailActionResult.SetReviewItemList(listOf()))
            }
        }
    }

    private fun loadFavoriteMedia() = viewModelScope.launch {
        val favoriteMedia = loadFavoriteMediaUseCase.loadById(mediaId)
        isFavorite = favoriteMedia != null
        handleActionResult(MediaDetailActionResult.SetFavorite(isFavorite))
    }

    fun changeFavoriteMedia() {
        if (isFavorite) deleteFavoriteMedia()
        else saveFavoriteMedia()
    }

    private fun saveFavoriteMedia() = viewModelScope.launch {
        media?.let {
            val res = saveFavoriteMediaUseCase(it)
            handleActionResult(MediaDetailActionResult.SetFavorite(true))
        }
    }

    private fun deleteFavoriteMedia() = viewModelScope.launch {
        media?.let {
            val res = deleteFavoriteMediaUseCase(it)
            handleActionResult(MediaDetailActionResult.SetFavorite(false))
        }
    }

    override fun reducer(oldState: MediaDetailViewState, actionResult: MediaDetailActionResult): MediaDetailViewState {
        return MediaDetailViewState(
            media = mediaReducer(actionResult),
            reviewItemList = reviewItemListReducer(actionResult),
            isMediaLoading = isMediaLoadingReducer(actionResult),
            isReviewLoading = isReviewLoadingReducer(actionResult),
            isFavoriteLoading = isFavoriteLoadingReducer(actionResult),
            isFavorite = favoriteReducer(actionResult),
            error = errorReducer(actionResult),
        )
    }

    private fun errorReducer(actionResult: MediaDetailActionResult): String? {
        return when (actionResult) {
            is MediaDetailActionResult.SetError -> actionResult.errorMsg
            else -> null
        }
    }

    private fun favoriteReducer(actionResult: MediaDetailActionResult): Boolean {
        if (actionResult is MediaDetailActionResult.SetFavorite) {
            isFavorite = actionResult.isFavorite
        }
        return when (actionResult) {
            is MediaDetailActionResult.SetFavoriteLoading -> false
            else -> isFavorite
        }
    }

    private fun mediaReducer(actionResult: MediaDetailActionResult): Media? {
        media = when (actionResult) {
            is MediaDetailActionResult.SetMediaLoading -> null
            else -> media
        }
        return media
    }

    private fun reviewItemListReducer(actionResult: MediaDetailActionResult): List<Review> {
        reviewItemList = when (actionResult) {
            is MediaDetailActionResult.SetReviewLoading -> emptyList()
            else -> reviewItemList
        }
        return reviewItemList
    }

    private fun isMediaLoadingReducer(actionResult: MediaDetailActionResult): Boolean {
        return when (actionResult) {
            is MediaDetailActionResult.SetMediaLoading -> actionResult.isShown
            else -> false
        }
    }

    private fun isReviewLoadingReducer(actionResult: MediaDetailActionResult): Boolean {
        return when (actionResult) {
            is MediaDetailActionResult.SetReviewLoading -> actionResult.isShown
            else -> false
        }
    }

    private fun isFavoriteLoadingReducer(actionResult: MediaDetailActionResult): Boolean {
        return when (actionResult) {
            is MediaDetailActionResult.SetFavoriteLoading -> actionResult.isShown
            else -> false
        }
    }
}