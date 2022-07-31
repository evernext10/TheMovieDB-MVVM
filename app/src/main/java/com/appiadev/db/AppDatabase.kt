package com.appiadev.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.appiadev.db.converters.IntegerListConverter
import com.appiadev.db.daos.MovieDao
import com.appiadev.model.api.Movie

@Database(
    entities = [(Movie::class)],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [(IntegerListConverter::class)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
