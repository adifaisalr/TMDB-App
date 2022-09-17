package com.adifaisalr.tmdbapplication.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SearchItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName(value = "title", alternate = ["name"])
    val title: String = "",
    @SerializedName("poster_path", alternate = ["profile_path"])
    val posterPath: String = "",
    @SerializedName(value = "release_date", alternate = ["first_air_date"])
    val releaseDate: String = "",
    @SerializedName("vote_average")
    val rating: Double = 0.0,
    @SerializedName("media_type")
    val mediaType: String
)
