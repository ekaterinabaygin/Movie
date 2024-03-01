package com.ekaterinabaygin.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieLocalEntity::class], version = 1, exportSchema = false)
internal abstract class MovieDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var instance: MovieDatabase? = null

        fun getInstance(context: Context) = synchronized(this) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie"
                ).build()
            }
            requireNotNull(instance)
        }
    }

    abstract fun movieDao(): MovieDao
}
