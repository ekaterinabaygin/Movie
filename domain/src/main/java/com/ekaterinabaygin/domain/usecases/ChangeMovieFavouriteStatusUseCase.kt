package com.ekaterinabaygin.domain.usecases

import com.ekaterinabaygin.domain.entity.MoviePreview
import com.ekaterinabaygin.domain.repository.MoviesRepository

class ChangeMovieFavouriteStatusUseCase(
    private val moviesRepository: MoviesRepository
) {

    suspend fun execute(movie: MoviePreview) = moviesRepository.markAsFavourite(
        movie = movie,
        isFavourite = !movie.isFavourite
    )
}
