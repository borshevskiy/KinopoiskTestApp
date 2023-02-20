package com.borshevskiy.kinopoisktestapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilmsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilms(currencyList: List<FilmDbModel>)

    @Query("SELECT * FROM all_films_list ORDER BY year ASC")
    suspend fun readFilmsYearAsc(): List<FilmDbModel>

    @Query("SELECT * FROM all_films_list ORDER BY year DESC")
    suspend fun readFilmsYearDesc(): List<FilmDbModel>

    @Query("DELETE FROM all_films_list")
    suspend fun deleteAll()
}