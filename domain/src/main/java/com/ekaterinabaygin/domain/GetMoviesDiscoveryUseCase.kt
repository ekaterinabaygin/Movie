package com.ekaterinabaygin.domain

import com.ekaterinabaygin.domain.common.UseCase
import com.ekaterinabaygin.domain.entity.MovieEntity
import com.ekaterinabaygin.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMoviesDiscoveryUseCase(
    private val moviesRepository: MovieRepository
) : UseCase<Unit, List<MovieEntity>> {

    override suspend fun execute(params: Unit) = withContext(Dispatchers.IO) {
        moviesRepository.getMovieDiscovery()
    }
}

