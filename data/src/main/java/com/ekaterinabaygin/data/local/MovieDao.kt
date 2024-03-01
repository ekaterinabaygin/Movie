package com.ekaterinabaygin.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieLocalEntity)

    @Query("DELETE FROM favourite_movies WHERE id=:id")
    suspend fun remove(id: String)

    @Query("SELECT * FROM favourite_movies ORDER BY insert_timestamp_ms DESC")
    fun getFavouriteMovies(): Flow<List<MovieLocalEntity>>

    @Query("SELECT id FROM favourite_movies")
    fun getFavouriteMovieIds(): Flow<List<String>>
}