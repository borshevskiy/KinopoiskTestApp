package com.borshevskiy.kinopoisktestapp.data.network.model


import com.google.gson.annotations.SerializedName

data class ExternalId(
    @SerializedName("_id")
    val id: String,
    @SerializedName("imdb")
    val imdb: String,
    @SerializedName("kpHD")
    val kpHD: String?,
    @SerializedName("tmdb")
    val tmdb: Int?
)