package com.adifaisalr.tmdbapplication.presentation.ui.media

import com.adifaisalr.tmdbapplication.domain.model.DiscoverMedia
import com.adifaisalr.tmdbapplication.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.domain.model.PopularMedia
import com.adifaisalr.tmdbapplication.domain.model.TrendingMedia
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

data class MediaHomeViewState(
    val popularMediaViewState: MediaViewState = MediaViewState(),
    val trendingMediaViewState: MediaViewState = MediaViewState(),
    val discoverMediaViewState: MediaViewState = MediaViewState(),
) : BaseViewModel.ViewState

data class MediaViewState(
    val media: HomeSectionMedia? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : BaseViewModel.ViewState