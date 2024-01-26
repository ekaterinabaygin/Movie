package com.ekaterinabaygin.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieApi {
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMzNhNDI2NTk2YWRjNGEwY2RkYmVlZDIzOTI3Njg1NSIsInN1YiI6IjY1YWFkNTYwOGQ1MmM5MDEzNzgxZjY5OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BDtNTt5PxJD-fdRxwcYMcbZTfXQJ-QCQkrp9jxPv1X0")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: MovieApiService = retrofit.create(MovieApiService::class.java)

}
