package com.ekaterinabaygin.domain.repository

import com.ekaterinabaygin.domain.entity.MovieEntity

interface MovieRepository {

    suspend fun getMovieDiscovery(): List<MovieEntity>
}
