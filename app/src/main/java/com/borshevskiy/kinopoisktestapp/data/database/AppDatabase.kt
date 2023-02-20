package com.borshevskiy.kinopoisktestapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FilmDbModel::class],version = 1,exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun filmsDao(): FilmsDao
}