package com.adifaisalr.tmdbapplication.presentation.ui.favorite

import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

sealed class FavoriteMediaActionResult : BaseViewModel.ActionResult {
    data class SetShowLoading(val isShown: Boolean) : FavoriteMediaActionResult()
    data class SetFavoriteMediaList(
        val favoriteMediaList: List<Media>,
    ) : FavoriteMediaActionResult()
}
