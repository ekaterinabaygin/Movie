package com.ekaterinabaygin.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ekaterinabaygin.domain.GetMoviesDiscoveryUseCase
import kotlinx.coroutines.Dispatchers

class MainViewModel(
    private val getMoviesDiscoveryUseCase: GetMoviesDiscoveryUseCase
) : ViewModel() {

    val moviesDiscovery = liveData(Dispatchers.IO) {
        emit(getMoviesDiscoveryUseCase.execute(Unit))
    }
}
