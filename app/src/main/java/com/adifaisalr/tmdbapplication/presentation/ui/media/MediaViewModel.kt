package com.adifaisalr.tmdbapplication.presentation.ui.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.domain.model.DiscoverMedia
import com.adifaisalr.tmdbapplication.domain.model.PopularMedia
import com.adifaisalr.tmdbapplication.domain.model.TrendingMedia
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.usecase.GetDiscoverMediaUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.GetPopularMediaUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.GetTrendingMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getPopularMediaUseCase: GetPopularMediaUseCase,
    private val getTrendingMediaUseCase: GetTrendingMediaUseCase,
    private val getDiscoverMediaUseCase: GetDiscoverMediaUseCase
) : ViewModel() {

    var mediaType: MediaType = MediaType.MOVIES

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val _popularMovieResult = MutableLiveData<DataHolder<PopularMedia>>()
    val popularMovieResult: LiveData<DataHolder<PopularMedia>>
        get() = _popularMovieResult

    val _trendingMovieResult = MutableLiveData<DataHolder<TrendingMedia>>()
    val trendingMovieResult: LiveData<DataHolder<TrendingMedia>>
        get() = _trendingMovieResult

    val _discoverMovieResult = MutableLiveData<DataHolder<DiscoverMedia>>()
    val discoverMovieResult: LiveData<DataHolder<DiscoverMedia>>
        get() = _discoverMovieResult

    fun getPopularMovies() = viewModelScope.launch {
        _popularMovieResult.postValue(DataHolder.Loading)
        val response = getPopularMediaUseCase(mediaType)
        _popularMovieResult.postValue(response)
    }

    fun getTrendingMovies() = viewModelScope.launch {
        _trendingMovieResult.postValue(DataHolder.Loading)
        val response = getTrendingMediaUseCase(mediaType.type, TIME_WINDOW_DAY)
        _trendingMovieResult.postValue(response)
    }

    fun getDiscoverMovies() = viewModelScope.launch {
        _discoverMovieResult.postValue(DataHolder.Loading)
        val response = getDiscoverMediaUseCase(mediaType)
        _discoverMovieResult.postValue(response)
    }

    companion object {
        enum class MediaType(val id: Int, val type: String, val titleStringId: Int) {
            MOVIES(0, "movie", R.string.title_movies),
            TV_SHOWS(1, "tv", R.string.title_tvs)
        }

        const val TIME_WINDOW_DAY = "day"
    }
}