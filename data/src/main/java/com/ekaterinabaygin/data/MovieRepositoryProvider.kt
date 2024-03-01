package com.ekaterinabaygin.data

import android.content.Context
import com.ekaterinabaygin.data.local.MovieDatabase
import com.ekaterinabaygin.data.remote.MovieService
import com.ekaterinabaygin.domain.repository.MoviesRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieRepositoryProvider {

    fun provide(context: Context): MoviesRepository = MovieRepositoryImpl(
        api = MovieApiProvider.movieApi,
        dao = MovieDatabase.getInstance(context).movieDao()
    )
}

private object MovieApiProvider {

    private val authInterceptor = Interceptor { chain ->
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

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()

    private val retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val movieApi: MovieService = retrofit.create(MovieService::class.java)
}
