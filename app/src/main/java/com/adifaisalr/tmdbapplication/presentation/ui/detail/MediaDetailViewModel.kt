package com.adifaisalr.tmdbapplication.presentation.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.MediaReview
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.usecase.GetMediaDetailUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.GetMediaReviewUseCase
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    private val getMediaDetailUseCase: GetMediaDetailUseCase,
    private val getMediaReviewUseCase: GetMediaReviewUseCase
) : ViewModel() {

    var mediaId by Delegates.notNull<Int>()
    lateinit var mediaType: MediaViewModel.Companion.MediaType

    val _movieDetailResult = MutableLiveData<DataHolder<Media>>()
    val movieDetailResult: LiveData<DataHolder<Media>>
        get() = _movieDetailResult

    val _movieReviewResult = MutableLiveData<DataHolder<MediaReview>>()
    val movieReviewResult: LiveData<DataHolder<MediaReview>>
        get() = _movieReviewResult

    fun getMovieDetail() = viewModelScope.launch {
        _movieDetailResult.postValue(DataHolder.Loading)
        val response = getMediaDetailUseCase(mediaType, mediaId)
        _movieDetailResult.postValue(response)
    }

    fun getMovieReviews() = viewModelScope.launch {
        _movieReviewResult.postValue(DataHolder.Loading)
        val response = getMediaReviewUseCase(mediaType, mediaId)
        _movieReviewResult.postValue(response)
    }

    fun getReviews() = movieReviewResult.value?.peekData?.reviews ?: listOf()
}