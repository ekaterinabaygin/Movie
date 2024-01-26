package com.ekaterinabaygin.domain

interface MovieRepository {
    suspend fun getMoviesDiscovery(): Result<List<MovieEntity>>
}
