package com.ekaterinabaygin.movie.feature.favourite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ekaterinabaygin.data.MovieRepositoryProvider
import com.ekaterinabaygin.domain.entity.MoviePreview
import com.ekaterinabaygin.domain.usecases.ChangeMovieFavouriteStatusUseCase
import com.ekaterinabaygin.domain.usecases.GetFavouritesMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoriteMoviesUIState(
    val moviesState: MovieState
) {

    companion object {
        val INITIAL = FavoriteMoviesUIState(moviesState = MovieState.Loading)
    }

    sealed class MovieState {
        data object Loading : MovieState()
        data class Data(val movies: List<MoviePreview>) : MovieState()
    }
}

class FavoriteMoviesViewModelFactory(
    private val app: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FavoriteMoviesViewModel::class.java)
        val repository = MovieRepositoryProvider.provide(app)
        val getFavouritesMovies = GetFavouritesMoviesUseCase(repository)
        val changeMovieFavouriteStatus = ChangeMovieFavouriteStatusUseCase(repository)
        return FavoriteMoviesViewModel(getFavouritesMovies, changeMovieFavouriteStatus) as T
    }
}

class FavoriteMoviesViewModel(
    private val getFavouritesMovies: GetFavouritesMoviesUseCase,
    private val changeMovieFavouriteStatus: ChangeMovieFavouriteStatusUseCase
) : ViewModel() {

    private val mutableState = MutableStateFlow(FavoriteMoviesUIState.INITIAL)
    val state = mutableState.asStateFlow()

    init {
        loadMovies()
    }

    fun switchFavouriteStatus(movie: MoviePreview) = viewModelScope.launch {
        changeMovieFavouriteStatus.execute(movie)
    }

    private fun loadMovies() {
        getFavouritesMovies.execute()
            .map { FavoriteMoviesUIState.MovieState.Data(it) as FavoriteMoviesUIState.MovieState }
            .onStart { emit(FavoriteMoviesUIState.MovieState.Loading) }
            .onEach { state -> mutableState.update { it.copy(moviesState = state) } }
            .launchIn(viewModelScope)
    }
}
