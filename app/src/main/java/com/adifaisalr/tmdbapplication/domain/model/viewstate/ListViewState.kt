package com.adifaisalr.tmdbapplication.domain.model.viewstate

import android.view.View
import androidx.paging.PagingData
import com.adifaisalr.tmdbapplication.domain.model.SearchItem

data class ListViewState(
    val page: PagingData<SearchItem>? = null,
    val adapterList: List<SearchItem> = emptyList(),
    val errorMessageResource: Int? = null,
    val errorMessage: String? = null,
    val loadingStateVisibility: Int? = View.GONE,
    val errorVisibility: Int? = View.GONE
)
