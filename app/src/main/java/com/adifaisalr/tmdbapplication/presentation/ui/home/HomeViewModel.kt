package com.adifaisalr.tmdbapplication.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.domain.model.DiscoverMovieResponse
import com.adifaisalr.tmdbapplication.domain.model.TrendingResponse
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.usecase.GetDiscoverMovieUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.GetTrendingMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMediaUseCase: GetTrendingMediaUseCase,
    private val getDiscoverMovieUseCase: GetDiscoverMovieUseCase
) : ViewModel() {

    var mediaType: String = MEDIA_TYPE_MOVIE

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val _trendingMovieResult = MutableLiveData<DataHolder<TrendingResponse>>()
    val trendingMovieResult: LiveData<DataHolder<TrendingResponse>>
        get() = _trendingMovieResult

    val _discoverMovieResult = MutableLiveData<DataHolder<DiscoverMovieResponse>>()
    val discoverMovieResult: LiveData<DataHolder<DiscoverMovieResponse>>
        get() = _discoverMovieResult

    fun getTrendingMovies() = viewModelScope.launch {
        _trendingMovieResult.postValue(DataHolder.Loading)
        val response = getTrendingMediaUseCase(mediaType, TIME_WINDOW_DAY)
        _trendingMovieResult.postValue(response)
    }

    fun getDiscoverMovies() = viewModelScope.launch {
        _discoverMovieResult.postValue(DataHolder.Loading)
        val response = getDiscoverMovieUseCase()
        _discoverMovieResult.postValue(response)
    }

    companion object {
        const val MEDIA_TYPE_MOVIE = "movie"
        const val MEDIA_TYPE_TV = "tv"
        const val TIME_WINDOW_DAY = "day"
    }
}