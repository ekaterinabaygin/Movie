package com.ekaterinabaygin.data

import com.google.gson.annotations.SerializedName

data class MovieResponseDto(
    @SerializedName("JSON")
    val results: List<MoviePreviewDto>
)