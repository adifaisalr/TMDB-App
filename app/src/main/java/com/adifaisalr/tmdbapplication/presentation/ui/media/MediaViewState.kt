package com.adifaisalr.tmdbapplication.presentation.ui.media

import com.adifaisalr.tmdbapplication.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

data class MediaViewState(
    val popularMediaViewState: MediaSectionViewState = MediaSectionViewState(),
    val trendingMediaViewState: MediaSectionViewState = MediaSectionViewState(),
    val discoverMediaViewState: MediaSectionViewState = MediaSectionViewState(),
) : BaseViewModel.ViewState

data class MediaSectionViewState(
    val media: HomeSectionMedia? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : BaseViewModel.ViewState