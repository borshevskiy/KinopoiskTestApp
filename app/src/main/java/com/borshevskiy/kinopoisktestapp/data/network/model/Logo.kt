package com.borshevskiy.kinopoisktestapp.data.network.model


import com.google.gson.annotations.SerializedName

data class Logo(
    @SerializedName("_id")
    val id: String,
    @SerializedName("url")
    val url: String?
)