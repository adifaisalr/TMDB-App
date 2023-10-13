package com.adifaisalr.tmdbapplication.presentation.ui.detail

import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.Review
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

data class MediaDetailViewState(
    val media: Media? = null,
    val reviewItemList: List<Review> = listOf(),
    val isMediaLoading: Boolean = false,
    val isReviewLoading: Boolean = false,
    val isFavoriteLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val error: String? = null,
) : BaseViewModel.ViewState