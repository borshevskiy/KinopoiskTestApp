package com.borshevskiy.kinopoisktestapp.domain

import com.borshevskiy.kinopoisktestapp.domain.util.NetworkResult

interface FilmRepository {

    suspend fun getFilmsList(sortQuery: String): NetworkResult<List<FilmInfo>>

    suspend fun searchFilms(sortQuery: String, searchQuery: String): NetworkResult<List<FilmInfo>>

    suspend fun getDBCache(sortQuery: String): List<FilmInfo>

}