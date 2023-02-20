package com.borshevskiy.kinopoisktestapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_films_list")
data class FilmDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val year: Int,
    val description: String,
    val rating: Double,
    val imageUrl: String
)