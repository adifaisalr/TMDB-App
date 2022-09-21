package com.adifaisalr.tmdbapplication.presentation.ui.search

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.domain.model.SearchMedia
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.model.viewstate.ListViewState
import com.adifaisalr.tmdbapplication.domain.usecase.SearchMediaPagingUseCase
import com.adifaisalr.tmdbapplication.domain.usecase.SearchMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val searchMediaPagingUseCase: SearchMediaPagingUseCase
) : ViewModel() {

    private var _query = ""
    val query: String
        get() = _query

    val _listViewStateLiveData: MutableLiveData<ListViewState> = MutableLiveData()
    val listViewStateLiveData: LiveData<ListViewState>
        get() = _listViewStateLiveData

    var listViewState: ListViewState = ListViewState()
        set(value) {
            field = value
            _listViewStateLiveData.postValue(value)
        }

    fun setQuery(originalInput: String) {
        val input = originalInput.lowercase(Locale.getDefault()).trim()
        if (input == _query) {
            return
        }
        _query = input
    }

    fun searchMediaPaging() = viewModelScope.launch {
        dataHolderToViewState(DataHolder.Loading)
        searchMediaPagingUseCase(query)
            .cachedIn(viewModelScope)
            .onEach { ddf -> Timber.d("onScreenLoad returned FLOW: $ddf") }
            .collect { results ->
                Timber.d("collect: $results")
                dataHolderToViewState(DataHolder.Success(results))
            }
    }

    fun dataHolderToViewState(dataHolder: DataHolder<PagingData<SearchItem>>) {
        val current = when (dataHolder) {
            is DataHolder.Loading -> {
                listViewState.copy(
                    loadingStateVisibility = View.VISIBLE,
                    errorVisibility = View.GONE
                )
            }
            is DataHolder.Success -> {
                listViewState.copy(
                    page = dataHolder.data,
                    loadingStateVisibility = View.GONE,
                    errorVisibility = View.GONE
                )
            }
            else -> {
                listViewState.copy(
                    errorVisibility = View.VISIBLE,
                    errorMessage = dataHolder.peekError?.message,
                    loadingStateVisibility = View.GONE
                )
            }
        }
        _listViewStateLiveData.postValue(current)
    }

    companion object {
        const val SAVED_STATE_SEARCH_RESULT = "search_result"
        const val SAVED_STATE_LOADMORE_RESULT = "load_more_result"
        const val SAVED_STATE_CURRENT_PAGE = "current_page"
        const val PER_PAGE = 30
    }
}