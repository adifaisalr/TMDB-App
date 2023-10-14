package com.adifaisalr.tmdbapplication.presentation.ui.search

import com.adifaisalr.tmdbapplication.libs.domain.model.MediaType
import com.adifaisalr.tmdbapplication.libs.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

sealed class SearchActionResult : BaseViewModel.ActionResult {
    data class SetShowLoading(val isShown: Boolean) : SearchActionResult()
    data class SetSearchItemList(
        val searchItemList: List<SearchItem>,
        val isLastBatch: Boolean?,
    ) : SearchActionResult()

    data class GoToDetail(
        val id: Long,
        val type: MediaType,
    ) : SearchActionResult()

    data object DoNothing : SearchActionResult()
    data object RefreshState : SearchActionResult()
}
