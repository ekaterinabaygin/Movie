package com.ekaterinabaygin.data.provider

import com.ekaterinabaygin.data.remote.MovieService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieApiProvider {

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val resultRequest = chain.request()
                .newBuilder()
                .header("accept", "application/json")
                .header(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMzNhNDI2NTk2YWRjNGEwY2RkYmVlZDIzOTI3Njg1NSIsInN1YiI6IjY1YWFkNTYwOGQ1MmM5MDEzNzgxZjY5OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BDtNTt5PxJD-fdRxwcYMcbZTfXQJ-QCQkrp9jxPv1X0"
                )
                .build()
            chain.proceed(resultRequest)
        }
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()

    private val retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val movieApi: MovieService = retrofit.create(MovieService::class.java)
}
