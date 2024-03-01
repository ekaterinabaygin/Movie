package com.ekaterinabaygin.domain.usecases

import com.ekaterinabaygin.domain.repository.MoviesRepository

class GetFavouritesMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {

    fun execute() = moviesRepository.getFavouriteMovies()
}
