package com.borshevskiy.kinopoisktestapp.data.network.model


import com.google.gson.annotations.SerializedName

data class Watchability(
    @SerializedName("_id")
    val id: String,
    @SerializedName("items")
    val items: List<Item>?
)