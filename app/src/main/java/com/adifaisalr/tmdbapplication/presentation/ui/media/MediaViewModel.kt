package com.adifaisalr.tmdbapplication.presentation.ui.media

import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.libs.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.libs.domain.model.MediaType
import com.adifaisalr.tmdbapplication.libs.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.libs.domain.usecase.GetDiscoverMediaUseCase
import com.adifaisalr.tmdbapplication.libs.domain.usecase.GetPopularMediaUseCase
import com.adifaisalr.tmdbapplication.libs.domain.usecase.GetTrendingMediaUseCase
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getPopularMediaUseCase: GetPopularMediaUseCase,
    private val getTrendingMediaUseCase: GetTrendingMediaUseCase,
    private val getDiscoverMediaUseCase: GetDiscoverMediaUseCase,
) : BaseViewModel<MediaViewState, MediaActionResult>(
    initialState = MediaViewState()
) {

    var mediaType: MediaType = MediaType.MOVIES

    private var popularMedia: HomeSectionMedia? = null
    private var trendingMedia: HomeSectionMedia? = null
    private var discoverMedia: HomeSectionMedia? = null

    fun fetchAllData() {
        getPopularMovies()
        getTrendingMovies()
        getDiscoverMovies()
    }

    override fun reducer(oldState: MediaViewState, actionResult: MediaActionResult): MediaViewState {
        return MediaViewState(
            popularMediaViewState = oldState.popularMediaReducer(actionResult),
            trendingMediaViewState = oldState.trendingMediaReducer(actionResult),
            discoverMediaViewState = oldState.discoverMediaReducer(actionResult),
        )
    }

    private fun MediaViewState.popularMediaReducer(actionResult: MediaActionResult): MediaSectionViewState =
        when (actionResult) {
            is MediaActionResult.SetPopularLoading -> MediaSectionViewState(isLoading = true)
            is MediaActionResult.SetPopularMediaHome -> MediaSectionViewState(media = actionResult.popularMedia)
            is MediaActionResult.SetPopularError -> MediaSectionViewState(errorMessage = actionResult.errorMsg)
            else -> popularMediaViewState
        }

    private fun MediaViewState.trendingMediaReducer(actionResult: MediaActionResult): MediaSectionViewState =
        when (actionResult) {
            is MediaActionResult.SetTrendingLoading -> MediaSectionViewState(isLoading = true)
            is MediaActionResult.SetTrendingMediaHome -> MediaSectionViewState(media = actionResult.trendingMedia)
            is MediaActionResult.SetTrendingError -> MediaSectionViewState(errorMessage = actionResult.errorMsg)
            else -> trendingMediaViewState
        }

    private fun MediaViewState.discoverMediaReducer(actionResult: MediaActionResult): MediaSectionViewState =
        when (actionResult) {
            is MediaActionResult.SetDiscoverLoading -> MediaSectionViewState(isLoading = true)
            is MediaActionResult.SetDiscoverMediaHome -> MediaSectionViewState(media = actionResult.discoverMedia)
            is MediaActionResult.SetDiscoverError -> MediaSectionViewState(errorMessage = actionResult.errorMsg)
            else -> discoverMediaViewState
        }

    private fun getPopularMovies() = viewModelScope.launch {
        handleActionResult(MediaActionResult.SetPopularLoading(true))
        when (val response = getPopularMediaUseCase(mediaType)) {
            is DataHolder.Success -> {
                popularMedia = response.data
                handleActionResult(MediaActionResult.SetPopularMediaHome(popularMedia))
            }

            else -> {
                handleActionResult(MediaActionResult.SetPopularError("Error loading data"))
            }
        }
    }

    private fun getTrendingMovies() = viewModelScope.launch {
        handleActionResult(MediaActionResult.SetTrendingLoading(true))
        when (val response = getTrendingMediaUseCase(mediaType.type, TIME_WINDOW_DAY)) {
            is DataHolder.Success -> {
                trendingMedia = response.data
                handleActionResult(MediaActionResult.SetTrendingMediaHome(trendingMedia))
            }

            else -> {
                handleActionResult(MediaActionResult.SetTrendingError("Error loading data"))
            }
        }
    }

    private fun getDiscoverMovies() = viewModelScope.launch {
        handleActionResult(MediaActionResult.SetDiscoverLoading(true))
        when (val response = getDiscoverMediaUseCase(mediaType)) {
            is DataHolder.Success -> {
                discoverMedia = response.data
                handleActionResult(MediaActionResult.SetDiscoverMediaHome(discoverMedia))
            }

            else -> {
                handleActionResult(MediaActionResult.SetDiscoverError("Error loading data"))
            }
        }
    }

    companion object {
        const val TIME_WINDOW_DAY = "day"
    }
}

