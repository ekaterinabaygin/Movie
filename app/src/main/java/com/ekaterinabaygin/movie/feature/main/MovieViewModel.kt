package com.ekaterinabaygin.movie.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekaterinabaygin.data.MovieRepositoryImpl
import com.ekaterinabaygin.data.provider.MovieApiProvider
import com.ekaterinabaygin.domain.GetMoviesDiscoveryUseCase
import com.ekaterinabaygin.domain.common.executeSafely
import com.ekaterinabaygin.domain.entity.MovieEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieUIState(
    val moviesState: MovieState
) {

    companion object {
        val INITIAL = MovieUIState(
            moviesState = MovieState.Loading
        )
    }

    sealed class MovieState {
        data object Loading : MovieState()
        data class Data(val movies: List<MovieEntity>) : MovieState()
        data object Error : MovieState()
    }
}

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val getMoviesDiscoveryUseCase = GetMoviesDiscoveryUseCase(
        moviesRepository = MovieRepositoryImpl(
            moviesApiService = MovieApiProvider.movieApi
        )
    )

    private val mutableState = MutableStateFlow(MovieUIState.INITIAL)
    val state = mutableState.asStateFlow()

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            val resultState = getMoviesDiscoveryUseCase.executeSafely(Unit).fold(
                onSuccess = MovieUIState.MovieState::Data,
                onFailure = {
                    Log.e(TAG, "Failed to load movies", it)
                    MovieUIState.MovieState.Error
                }
            )
            mutableState.update { it.copy(moviesState = resultState) }
        }
        mutableState.update { it.copy(moviesState = MovieUIState.MovieState.Loading) }
    }
}
