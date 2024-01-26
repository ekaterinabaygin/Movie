package com.ekaterinabaygin.domain

class GetMoviesDiscoveryUseCase(
    private val moviesRepository: MovieRepository
) : SafeResultUseCase<Unit, List<MovieEntity>>() {

    override suspend fun doWork(params: Unit): List<MovieEntity> {
        return moviesRepository.getMoviesDiscovery().getOrThrow()
    }
}

