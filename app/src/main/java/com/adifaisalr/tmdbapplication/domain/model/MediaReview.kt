package com.adifaisalr.tmdbapplication.domain.model

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class MediaReview(
    @SerializedName("id")
    val id: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val reviews: List<Review>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)