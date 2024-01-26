package com.ekaterinabaygin.data.remote

import com.google.gson.annotations.SerializedName

data class MovieDiscoveryDto(
    @SerializedName("results")
    val results: List<MoviePreviewDto>
)

data class MoviePreviewDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String
)
