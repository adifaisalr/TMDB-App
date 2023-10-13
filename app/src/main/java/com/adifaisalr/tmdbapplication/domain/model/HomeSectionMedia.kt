package com.adifaisalr.tmdbapplication.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class HomeSectionMedia(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Media>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)