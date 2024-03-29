package com.adifaisalr.tmdbapplication.presentation.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.libs.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.libs.domain.model.SearchMedia
import com.adifaisalr.tmdbapplication.libs.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.libs.domain.usecase.SearchMediaUseCase
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val searchMediaUseCase: SearchMediaUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<SearchViewState, SearchActionResult>(
    initialState = SearchViewState()
) {

    private var query = ""
    private var isSearched = false
    protected var searchItemList: List<SearchItem> = emptyList()

    fun setSearched(searched: Boolean) {
        isSearched = searched
    }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == query) {
            return
        }
        searchItemList = emptyList()
        query = input
    }

    fun loadNextPage() = viewModelScope.launch {
        if (query.isBlank()) return@launch
        handleActionResult(SearchActionResult.SetShowLoading(true))
        when (val response = searchMediaUseCase(query, (searchItemList.count() / PER_PAGE) + 1)) {
            is DataHolder.Success -> {
                val newData = response.data?.results ?: listOf()
                handleActionResult(
                    SearchActionResult.SetSearchItemList(
                        searchItemList = newData,
                        isLastBatch = newData.size < PER_PAGE,
                    )
                )
            }

            else -> {}
        }
        isSearched = true
    }

    private fun searchItemListReducer(actionResult: SearchActionResult): List<SearchItem> {
        searchItemList = when (actionResult) {
            SearchActionResult.RefreshState -> emptyList()
            is SearchActionResult.SetSearchItemList -> searchItemList + actionResult.searchItemList

            else -> searchItemList
        }
        return searchItemList
    }

    private fun isLoadingReducer(actionResult: SearchActionResult): Boolean {
        return when (actionResult) {
            is SearchActionResult.SetShowLoading -> actionResult.isShown
            SearchActionResult.RefreshState -> true
            else -> false
        }
    }

    private fun SearchViewState.isLastBatchReducer(actionResult: SearchActionResult): Boolean =
        (actionResult as? SearchActionResult.SetSearchItemList)?.isLastBatch
            ?: isLastBatch

    override fun reducer(oldState: SearchViewState, actionResult: SearchActionResult): SearchViewState {
        return SearchViewState(
            searchItemList = searchItemListReducer(actionResult),
            isLoading = isLoadingReducer(actionResult),
            isLastBatch = oldState.isLastBatchReducer(actionResult),
            isSearched = isSearched,
        )
    }

    companion object {
        const val SAVED_STATE_SEARCH_RESULT = "search_result"
        const val SAVED_STATE_LOADMORE_RESULT = "load_more_result"
        const val SAVED_STATE_CURRENT_PAGE = "current_page"
        const val SAVED_STATE_SEARCH_ITEM = "search_items"
        const val PER_PAGE = 20
    }
}