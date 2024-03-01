package com.ekaterinabaygin.domain.usecases

import com.ekaterinabaygin.domain.repository.MoviesRepository

class GetMoviesDiscoveryUseCase(
    private val moviesRepository: MoviesRepository
) {

    fun execute() = moviesRepository.getMovieDiscovery()
}