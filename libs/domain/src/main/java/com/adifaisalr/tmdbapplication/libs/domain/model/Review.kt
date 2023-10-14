package com.adifaisalr.tmdbapplication.libs.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Review(
    @SerializedName("author")
    val author: String,
    @SerializedName("author_details")
    val authorDetails: AuthorDetails,
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("url")
    val url: String
) {
    @Keep
    data class AuthorDetails(
        @SerializedName("avatar_path")
        val avatarPath: String?,
        @SerializedName("name")
        val name: String,
        @SerializedName("rating")
        val rating: Double,
        @SerializedName("username")
        val username: String
    )
}