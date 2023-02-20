package com.borshevskiy.kinopoisktestapp.data.network

import com.borshevskiy.kinopoisktestapp.data.network.model.FilmInfoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /** Получаю список из 100 фильмов(для красоты картинки в рейтинге от 8-10) **/

    @GET("/movie")
    suspend fun getAllFilms(
        @Query("sortType") sortType: String,
        @Query("field") field: String = "rating.kp",
        @Query("search") search: String = "8-10"
    ): Response<FilmInfoDto>

    /** Поиск фильма по имени **/

    @GET("movie")
    suspend fun getFilmBySearchedName(
        @Query("sortType") sortType: String,
        @Query("field")field: String = "name",
        @Query("search")search: String
    ): Response<FilmInfoDto>
}