package com.ekaterinabaygin.data.remote

import retrofit2.http.GET

interface MovieService {

    @GET("3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc")
    suspend fun getMoviesDiscovery(): MovieDiscoveryDto
}
