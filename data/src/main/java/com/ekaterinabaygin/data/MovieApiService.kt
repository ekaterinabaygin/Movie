package com.ekaterinabaygin.data

import retrofit2.Response
import retrofit2.http.GET

interface MovieApiService {

    @GET("3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc")
    suspend fun getMoviesDiscovery(): Response<MovieResponseDto>
}
