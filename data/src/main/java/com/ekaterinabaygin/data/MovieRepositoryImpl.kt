package com.ekaterinabaygin.data

import com.ekaterinabaygin.data.local.MovieDao
import com.ekaterinabaygin.data.mapper.toDomain
import com.ekaterinabaygin.data.mapper.toLocal
import com.ekaterinabaygin.data.remote.MovieService
import com.ekaterinabaygin.domain.entity.MoviePreview
import com.ekaterinabaygin.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class MovieRepositoryImpl(
    private val api: MovieService,
    private val dao: MovieDao
) : MoviesRepository {

    override fun getMovieDiscovery() = flow { emit(api.getMoviesDiscovery().results) }
        .combine(getFavouriteMovieIds()) { discovery, favouriteIds ->
            discovery.map { it.toDomain(isFavourite = it.id in favouriteIds) }
        }

    override fun getMovieDetails(id: String) = flow { emit(api.getMovieDetails(id)) }
        .combine(getFavouriteMovieIds()) { details, favouriteIds ->
            details.toDomain(isFavourite = details.id in favouriteIds)
        }

    override fun getFavouriteMovies(): Flow<List<MoviePreview>> {
        return dao.getFavouriteMovies().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun markAsFavourite(movie: MoviePreview, isFavourite: Boolean) {
        if (movie.isFavourite == isFavourite) return
        if (isFavourite) {
            dao.insert(movie.toLocal())
        } else {
            dao.remove(movie.id)
        }
    }

    private fun getFavouriteMovieIds() = dao.getFavouriteMovieIds().map { it.toSet() }
}