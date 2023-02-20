package com.borshevskiy.kinopoisktestapp.data.network.model


import com.google.gson.annotations.SerializedName

data class FilmInfoDto(
    @SerializedName("docs")
    val films: List<Film>,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)