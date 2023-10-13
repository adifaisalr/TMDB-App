package com.adifaisalr.tmdbapplication.presentation.ui.media

import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.usecase.GetDiscoverMediaUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.GetPopularMediaUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.GetTrendingMediaUseCase
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getPopularMediaUseCase: GetPopularMediaUseCase,
    private val getTrendingMediaUseCase: GetTrendingMediaUseCase,
    private val getDiscoverMediaUseCase: GetDiscoverMediaUseCase
) : BaseViewModel<MediaHomeViewState, MediaHomeActionResult>(
    initialState = MediaHomeViewState()
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

    override fun reducer(oldState: MediaHomeViewState, actionResult: MediaHomeActionResult): MediaHomeViewState {
        return MediaHomeViewState(
            popularMediaViewState = oldState.popularMediaReducer(actionResult),
            trendingMediaViewState = oldState.trendingMediaReducer(actionResult),
            discoverMediaViewState = oldState.discoverMediaReducer(actionResult),
        )
    }

    private fun MediaHomeViewState.popularMediaReducer(actionResult: MediaHomeActionResult): MediaViewState =
        when (actionResult) {
            is MediaHomeActionResult.SetPopularLoading -> MediaViewState(isLoading = true)
            is MediaHomeActionResult.SetPopularMediaHome -> MediaViewState(media = actionResult.popularMedia)
            is MediaHomeActionResult.SetPopularError -> MediaViewState(errorMessage = actionResult.errorMsg)
            else -> popularMediaViewState
        }

    private fun MediaHomeViewState.trendingMediaReducer(actionResult: MediaHomeActionResult): MediaViewState =
        when (actionResult) {
            is MediaHomeActionResult.SetTrendingLoading -> MediaViewState(isLoading = true)
            is MediaHomeActionResult.SetTrendingMediaHome -> MediaViewState(media = actionResult.trendingMedia)
            is MediaHomeActionResult.SetTrendingError -> MediaViewState(errorMessage = actionResult.errorMsg)
            else -> trendingMediaViewState
        }

    private fun MediaHomeViewState.discoverMediaReducer(actionResult: MediaHomeActionResult): MediaViewState =
        when (actionResult) {
            is MediaHomeActionResult.SetDiscoverLoading -> MediaViewState(isLoading = true)
            is MediaHomeActionResult.SetDiscoverMediaHome -> MediaViewState(media = actionResult.discoverMedia)
            is MediaHomeActionResult.SetDiscoverError -> MediaViewState(errorMessage = actionResult.errorMsg)
            else -> discoverMediaViewState
        }

    private fun getPopularMovies() = viewModelScope.launch {
        handleActionResult(MediaHomeActionResult.SetPopularLoading(true))
        when (val response = getPopularMediaUseCase(mediaType)) {
            is DataHolder.Success -> {
                popularMedia = response.data
                handleActionResult(MediaHomeActionResult.SetPopularMediaHome(popularMedia))
            }

            else -> {
                handleActionResult(MediaHomeActionResult.SetPopularError("Error loading data"))
            }
        }
    }

    private fun getTrendingMovies() = viewModelScope.launch {
        handleActionResult(MediaHomeActionResult.SetTrendingLoading(true))
        when (val response = getTrendingMediaUseCase(mediaType.type, TIME_WINDOW_DAY)) {
            is DataHolder.Success -> {
                trendingMedia = response.data
                handleActionResult(MediaHomeActionResult.SetTrendingMediaHome(trendingMedia))
            }

            else -> {
                handleActionResult(MediaHomeActionResult.SetTrendingError("Error loading data"))
            }
        }
    }

    private fun getDiscoverMovies() = viewModelScope.launch {
        handleActionResult(MediaHomeActionResult.SetDiscoverLoading(true))
        when (val response = getDiscoverMediaUseCase(mediaType)) {
            is DataHolder.Success -> {
                discoverMedia = response.data
                handleActionResult(MediaHomeActionResult.SetDiscoverMediaHome(discoverMedia))
            }

            else -> {
                handleActionResult(MediaHomeActionResult.SetDiscoverError("Error loading data"))
            }
        }
    }

    companion object {
        enum class MediaType(val id: Int, val type: String, val titleStringId: Int) {
            MOVIES(0, "movie", R.string.title_movies),
            TV_SHOWS(1, "tv", R.string.title_tvs)
        }

        const val TIME_WINDOW_DAY = "day"
    }
}