package com.adifaisalr.tmdbapplication.presentation.ui.media

import com.adifaisalr.tmdbapplication.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

sealed class MediaHomeActionResult : BaseViewModel.ActionResult {
    data class SetPopularLoading(val isShown: Boolean) : MediaHomeActionResult()
    data class SetTrendingLoading(val isShown: Boolean) : MediaHomeActionResult()
    data class SetDiscoverLoading(val isShown: Boolean) : MediaHomeActionResult()
    data class SetPopularError(val errorMsg: String?) : MediaHomeActionResult()
    data class SetTrendingError(val errorMsg: String?) : MediaHomeActionResult()
    data class SetDiscoverError(val errorMsg: String?) : MediaHomeActionResult()
    data class SetPopularMediaHome(
        val popularMedia: HomeSectionMedia?,
    ) : MediaHomeActionResult()

    data class SetTrendingMediaHome(
        val trendingMedia: HomeSectionMedia?,
    ) : MediaHomeActionResult()

    data class SetDiscoverMediaHome(
        val discoverMedia: HomeSectionMedia?,
    ) : MediaHomeActionResult()
}
