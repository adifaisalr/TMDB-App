package com.adifaisalr.tmdbapplication.presentation.ui.favorite

import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

data class FavoriteMediaViewState(
    val favoriteMediaList: List<Media> = listOf(),
    val isLoading: Boolean = false,
) : BaseViewModel.ViewState