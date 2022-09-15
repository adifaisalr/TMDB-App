package com.adifaisalr.tmdbapplication.domain.model

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class TrendingResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Media>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
) {
}