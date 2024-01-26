package com.ekaterinabaygin.data

import com.google.gson.annotations.SerializedName

data class MoviePreviewDto(

    @SerializedName("id")
    val id: String,

    @SerializedName("author")
    val author: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("poster_path")
    val posterPath: String


)
