package com.adifaisalr.tmdbapplication.presentation.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.domain.model.SearchMedia
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.usecase.SearchMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val searchMediaUseCase: SearchMediaUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _query = ""
    val query: String
        get() = _query

    val _searchResult: MutableLiveData<DataHolder<SearchMedia>> =
        savedStateHandle.getLiveData(SAVED_STATE_SEARCH_RESULT)
    val searchResult: LiveData<DataHolder<SearchMedia>>
        get() = _searchResult

    val _loadMoreResult: MutableLiveData<DataHolder<SearchMedia>> =
        savedStateHandle.getLiveData(SAVED_STATE_LOADMORE_RESULT)
    val loadMoreResult: LiveData<DataHolder<SearchMedia>>
        get() = _loadMoreResult

    private val totalPage: Int
        get() = searchResult.value?.peekData?.totalPages ?: 0

    private var currentPage = savedStateHandle.get<Int>(SAVED_STATE_CURRENT_PAGE) ?: 1

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query) {
            return
        }
        currentPage = 1
        _query = input
    }

    fun searchMedia() = viewModelScope.launch {
        _searchResult.postValue(DataHolder.Loading)
        val response = searchMediaUseCase(query, currentPage)
        _searchResult.postValue(response)
    }

    fun loadNextPage() = viewModelScope.launch {
        if ((currentPage * PER_PAGE) < totalPage && loadMoreResult.value !is DataHolder.Loading) {
            _loadMoreResult.postValue(DataHolder.Loading)
            val response = searchMediaUseCase(query, ++currentPage)
            _loadMoreResult.postValue(response)
        }
    }

    companion object {
        const val SAVED_STATE_SEARCH_RESULT = "search_result"
        const val SAVED_STATE_LOADMORE_RESULT = "load_more_result"
        const val SAVED_STATE_CURRENT_PAGE = "current_page"
        const val PER_PAGE = 30
    }
}