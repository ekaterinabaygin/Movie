package com.ekaterinabaygin.movie.feature.discovery

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ekaterinabaygin.data.MovieRepositoryProvider
import com.ekaterinabaygin.domain.entity.MoviePreview
import com.ekaterinabaygin.domain.usecases.ChangeMovieFavouriteStatusUseCase
import com.ekaterinabaygin.domain.usecases.GetMoviesDiscoveryUseCase
import com.ekaterinabaygin.movie.feature.discovery.MovieDiscoveryUIState.MovieState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class MovieDiscoveryUIState(
    val moviesState: MovieState
) {

    companion object {
        val INITIAL = MovieDiscoveryUIState(moviesState = MovieState.Loading)
    }

    sealed class MovieState {
        data object Loading : MovieState()
        data class Data(val movies: List<MoviePreview>) : MovieState()
        data object Error : MovieState()
    }
}

class MovieDiscoveryViewModelFactory(
    private val app: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == MovieDiscoveryViewModel::class.java)
        val repository = MovieRepositoryProvider.provide(app)
        val getMoviesDiscovery = GetMoviesDiscoveryUseCase(repository)
        val changeMovieFavouriteStatus = ChangeMovieFavouriteStatusUseCase(repository)
        return MovieDiscoveryViewModel(getMoviesDiscovery, changeMovieFavouriteStatus) as T
    }
}

class MovieDiscoveryViewModel(
    private val getMoviesDiscovery: GetMoviesDiscoveryUseCase,
    private val changeMovieFavouriteStatus: ChangeMovieFavouriteStatusUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "MovieDiscoveryViewModel"
    }

    private val mutableState = MutableStateFlow(MovieDiscoveryUIState.INITIAL)
    val state = mutableState.asStateFlow()

    private var loadMoviesJob: Job? = null

    init {
        loadMovies()
    }

    fun loadMovies() {
        loadMoviesJob?.cancel()
        loadMoviesJob = getMoviesDiscovery.execute()
            .map { MovieState.Data(it) as MovieState }
            .onStart { emit(MovieState.Loading) }
            .onEach { state -> mutableState.update { it.copy(moviesState = state) } }
            .catch { error ->
                Log.e(TAG, "Failed to load discovery", error)
                mutableState.update { it.copy(moviesState = MovieState.Error) }
            }
            .launchIn(viewModelScope)
    }

    fun switchFavouriteStatus(movie: MoviePreview) = viewModelScope.launch {
        changeMovieFavouriteStatus.execute(movie)
    }
}
