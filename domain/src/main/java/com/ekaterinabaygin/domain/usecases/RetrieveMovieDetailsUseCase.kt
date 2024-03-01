package com.ekaterinabaygin.domain.usecases

import com.ekaterinabaygin.domain.repository.MoviesRepository

class RetrieveMovieDetailsUseCase(
    private val moviesRepository: MoviesRepository
) {

    fun execute(movieId: String) = moviesRepository.getMovieDetails(movieId)
}
