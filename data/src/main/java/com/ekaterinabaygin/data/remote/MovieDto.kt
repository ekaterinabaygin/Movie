package com.ekaterinabaygin.data.remote

import com.google.gson.annotations.SerializedName

internal data class MovieDiscoveryDto(
    @SerializedName("results")
    val results: List<MoviePreviewDto>
)

internal data class MoviePreviewDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String
)

internal data class MovieDetailsDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("genres")
    val genres: List<MovieGenreDto>,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("overview")
    val overview: String
)

internal data class MovieGenreDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)
