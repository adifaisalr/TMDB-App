package com.adifaisalr.tmdbapplication.presentation.ui.search

import com.adifaisalr.tmdbapplication.libs.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

data class SearchViewState(
    val searchItemList: List<SearchItem> = listOf(),
    val isLastBatch: Boolean = false,
    val isLoading: Boolean = false,
    val isSearched: Boolean = false,
) : BaseViewModel.ViewState