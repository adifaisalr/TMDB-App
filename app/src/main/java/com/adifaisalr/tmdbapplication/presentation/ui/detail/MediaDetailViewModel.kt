package com.adifaisalr.tmdbapplication.presentation.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.domain.model.MovieDetailResponse
import com.adifaisalr.tmdbapplication.domain.model.MovieReviewsResponse
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.usecase.GetMovieDetailUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.GetMovieReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieReviewUseCase: GetMovieReviewUseCase
) : ViewModel() {

    var movieId by Delegates.notNull<Int>()

    val _movieDetailResult = MutableLiveData<DataHolder<MovieDetailResponse>>()
    val movieDetailResult: LiveData<DataHolder<MovieDetailResponse>>
        get() = _movieDetailResult

    val _movieReviewResult = MutableLiveData<DataHolder<MovieReviewsResponse>>()
    val movieReviewResult: LiveData<DataHolder<MovieReviewsResponse>>
        get() = _movieReviewResult

    fun getMovieDetail() = viewModelScope.launch {
        _movieDetailResult.postValue(DataHolder.Loading)
        val response = getMovieDetailUseCase(movieId)
        _movieDetailResult.postValue(response)
    }

    fun getMovieReviews() = viewModelScope.launch {
        _movieReviewResult.postValue(DataHolder.Loading)
        val response = getMovieReviewUseCase(movieId)
        _movieReviewResult.postValue(response)
    }

    fun getReviews() = movieReviewResult.value?.peekData?.reviews ?: listOf()
}