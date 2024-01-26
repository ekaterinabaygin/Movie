package com.ekaterinabaygin.data.mapper

import com.ekaterinabaygin.data.remote.MoviePreviewDto
import com.ekaterinabaygin.domain.entity.MovieEntity

internal fun MoviePreviewDto.toDomain() = MovieEntity(
    id = id,
    title = title,
    posterUrl = "https://image.tmdb.org/t/p/original/$posterPath"
)