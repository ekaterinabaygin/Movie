package com.ekaterinabaygin.data.mapper

import com.ekaterinabaygin.data.local.MovieLocalEntity
import com.ekaterinabaygin.data.remote.MovieDetailsDto
import com.ekaterinabaygin.data.remote.MoviePreviewDto
import com.ekaterinabaygin.domain.entity.MovieDetails
import com.ekaterinabaygin.domain.entity.MoviePreview

internal fun MoviePreviewDto.toDomain(isFavourite: Boolean) = MoviePreview(
    id = id,
    title = title,
    posterUrl = "https://image.tmdb.org/t/p/original/$posterPath",
    isFavourite = isFavourite
)

internal fun MovieDetailsDto.toDomain(isFavourite: Boolean) = MovieDetails(
    preview = MoviePreview(
        id = id,
        title = title,
        posterUrl = "https://image.tmdb.org/t/p/original/$posterPath",
        isFavourite = isFavourite
    ),
    genre = genres.firstOrNull()?.name ?: "",
    originalLanguage = originalLanguage,
    overview = overview
)

internal fun MovieLocalEntity.toDomain() = MoviePreview(
    id = id,
    title = title,
    posterUrl = posterUrl,
    isFavourite = true
)

internal fun MoviePreview.toLocal() = MovieLocalEntity(
    id = id,
    title = title,
    posterUrl = posterUrl
)