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

    val _mediaDetailResult = MutableLiveData<DataHolder<Media>>()
    val mediaDetailResult: LiveData<DataHolder<Media>>
        get() = _mediaDetailResult

    val _mediaReviewResult = MutableLiveData<DataHolder<MediaReview>>()
    val mediaReviewResult: LiveData<DataHolder<MediaReview>>
        get() = _mediaReviewResult

    fun getMovieDetail() = viewModelScope.launch {
        _mediaDetailResult.postValue(DataHolder.Loading)
        val response = getMediaDetailUseCase(mediaType, mediaId)
        _mediaDetailResult.postValue(response)
    }

    fun getMovieReviews() = viewModelScope.launch {
        _mediaReviewResult.postValue(DataHolder.Loading)
        val response = getMediaReviewUseCase(mediaType, mediaId)
        _mediaReviewResult.postValue(response)
    }

    fun getReviews() = mediaReviewResult.value?.peekData?.reviews ?: listOf()
}