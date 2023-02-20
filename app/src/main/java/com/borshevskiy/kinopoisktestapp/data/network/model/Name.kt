package com.borshevskiy.kinopoisktestapp.data.network.model


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String
)