package com.adifaisalr.tmdbapplication.presentation.ui.detail

import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.Review
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel

sealed class MediaDetailActionResult : BaseViewModel.ActionResult {
    data class SetMediaLoading(val isShown: Boolean) : MediaDetailActionResult()
    data class SetReviewLoading(val isShown: Boolean) : MediaDetailActionResult()
    data class SetFavoriteLoading(val isShown: Boolean) : MediaDetailActionResult()
    data class SetFavorite(val isFavorite: Boolean) : MediaDetailActionResult()
    data class SetMedia(val media: Media?) : MediaDetailActionResult()
    data class SetError(val errorMsg: String?) : MediaDetailActionResult()
    data class SetReviewItemList(
        val reviewItemList: List<Review>,
    ) : MediaDetailActionResult()

    data object DoNothing : MediaDetailActionResult()
}
