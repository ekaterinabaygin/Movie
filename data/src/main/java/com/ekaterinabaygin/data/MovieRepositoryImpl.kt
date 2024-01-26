package com.ekaterinabaygin.data

import com.ekaterinabaygin.data.mapper.toDomain
import com.ekaterinabaygin.data.remote.MovieService
import com.ekaterinabaygin.domain.entity.MovieEntity
import com.ekaterinabaygin.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val moviesApiService: MovieService,
) : MovieRepository {

    override suspend fun getMovieDiscovery(): List<MovieEntity> {
        val discovery = moviesApiService.getMoviesDiscovery()
        return discovery.results.map { it.toDomain() }
    }
}
