package com.adifaisalr.tmdbapplication.libs.domain.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
@Entity
data class Media(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName(value = "title", alternate = ["name"])
    val title: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName(value = "release_date", alternate = ["first_air_date"])
    val releaseDate: String,
    @SerializedName("vote_average")
    val rating: Double,
    var type: String,
    var isFavorite: Boolean = false,
)

enum class MediaType(val id: Int, val type: String, val titleStr: String) {
    MOVIES(0, "movie", "Movies"),
    TV_SHOWS(1, "tv", "TV Shows")
}

fun Int.getMediaType() = if (this == 0) MediaType.MOVIES else MediaType.TV_SHOWS
