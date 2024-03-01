package com.ekaterinabaygin.movie.feature.details

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ekaterinabaygin.data.MovieRepositoryProvider
import com.ekaterinabaygin.domain.entity.MovieDetails
import com.ekaterinabaygin.domain.usecases.ChangeMovieFavouriteStatusUseCase
import com.ekaterinabaygin.domain.usecases.RetrieveMovieDetailsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieDetailsUIState(
    val moviesState: MovieState
) {

    companion object {
        val INITIAL = MovieDetailsUIState(moviesState = MovieState.Loading)
    }

    sealed class MovieState {
        data object Loading : MovieState()
        data class Data(val details: MovieDetails) : MovieState()
        data object Error : MovieState()
    }
}

class MovieDetailsViewModelFactory(
    private val movieId: String,
    private val app: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == MovieDetailsViewModel::class.java)
        val repository = MovieRepositoryProvider.provide(app)
        val getMoviesDiscovery = RetrieveMovieDetailsUseCase(repository)
        val changeMovieFavouriteStatus = ChangeMovieFavouriteStatusUseCase(repository)
        return MovieDetailsViewModel(movieId, getMoviesDiscovery, changeMovieFavouriteStatus) as T
    }
}

class MovieDetailsViewModel(
    private val movieId: String,
    private val retrieveMovieDetails: RetrieveMovieDetailsUseCase,
    private val changeMovieFavouriteStatus: ChangeMovieFavouriteStatusUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "MovieDetailsViewModel"
    }

    private val mutableState = MutableStateFlow(MovieDetailsUIState.INITIAL)
    val state = mutableState.asStateFlow()

    private var loadDetailsJob: Job? = null

    init {
        loadDetails()
    }

    fun loadDetails() {
        loadDetailsJob?.cancel()
        loadDetailsJob = retrieveMovieDetails.execute(movieId)
            .map { MovieDetailsUIState.MovieState.Data(it) }
            .onEach { state -> mutableState.update { it.copy(moviesState = state) } }
            .catch { error ->
                Log.e(TAG, "Failed to load details", error)
                mutableState.update { it.copy(moviesState = MovieDetailsUIState.MovieState.Error) }
            }
            .launchIn(viewModelScope)
        mutableState.update { it.copy(moviesState = MovieDetailsUIState.MovieState.Loading) }
    }

    fun switchFavouriteStatus() = viewModelScope.launch {
        val movie = state.value.moviesState as? MovieDetailsUIState.MovieState.Data ?: return@launch
        changeMovieFavouriteStatus.execute(movie.details.preview)
    }
}