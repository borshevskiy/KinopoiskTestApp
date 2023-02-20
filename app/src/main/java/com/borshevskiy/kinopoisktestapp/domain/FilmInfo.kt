package com.borshevskiy.kinopoisktestapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilmInfo(
    val id: Int,
    val name: String,
    val year: Int,
    val description: String,
    val rating: Double,
    val imageUrl: String
): Parcelable
