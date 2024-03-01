package com.ekaterinabaygin.domain.repository

import com.ekaterinabaygin.domain.entity.MovieDetails
import com.ekaterinabaygin.domain.entity.MoviePreview
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovieDiscovery(): Flow<List<MoviePreview>>

    fun getMovieDetails(id: String): Flow<MovieDetails>

    fun getFavouriteMovies(): Flow<List<MoviePreview>>

    suspend fun markAsFavourite(movie: MoviePreview, isFavourite: Boolean)
}
