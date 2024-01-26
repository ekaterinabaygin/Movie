package com.ekaterinabaygin.data

import com.ekaterinabaygin.domain.MovieEntity
import com.ekaterinabaygin.domain.MovieRepository

class MovieRepositoryImpl(
    private val moviesApiService: MovieApiService,
    private val movieMapper: MovieMapper
) : MovieRepository {

    override suspend fun getMoviesDiscovery(): Result<List<MovieEntity>> {
        return runCatching {
            val response = moviesApiService.getMoviesDiscovery()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.results.map(movieMapper::mapDtoToEntity)
            } else {
                throw Exception("API call failed with error: ${response.message()}")
            }
        }
    }
}
