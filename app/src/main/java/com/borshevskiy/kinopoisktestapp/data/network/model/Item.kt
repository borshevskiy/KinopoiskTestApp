package com.borshevskiy.kinopoisktestapp.data.network.model


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("_id")
    val id: String,
    @SerializedName("logo")
    val logo: LogoX,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)