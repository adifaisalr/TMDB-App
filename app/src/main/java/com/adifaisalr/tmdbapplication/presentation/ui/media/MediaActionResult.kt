package com.adifaisalr.tmdbapplication.presentation.ui.media

import com.adifaisalr.tmdbapplication.libs.domain.model.HomeSectionMedia
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

sealed class MediaActionResult : BaseViewModel.ActionResult {
    data class SetPopularLoading(val isShown: Boolean) : MediaActionResult()
    data class SetTrendingLoading(val isShown: Boolean) : MediaActionResult()
    data class SetDiscoverLoading(val isShown: Boolean) : MediaActionResult()
    data class SetPopularError(val errorMsg: String?) : MediaActionResult()
    data class SetTrendingError(val errorMsg: String?) : MediaActionResult()
    data class SetDiscoverError(val errorMsg: String?) : MediaActionResult()
    data class SetPopularMediaHome(
        val popularMedia: HomeSectionMedia?,
    ) : MediaActionResult()

    data class SetTrendingMediaHome(
        val trendingMedia: HomeSectionMedia?,
    ) : MediaActionResult()

    data class SetDiscoverMediaHome(
        val discoverMedia: HomeSectionMedia?,
    ) : MediaActionResult()
}
