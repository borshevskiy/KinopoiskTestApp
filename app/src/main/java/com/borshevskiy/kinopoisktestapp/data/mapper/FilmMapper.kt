package com.borshevskiy.kinopoisktestapp.data.mapper

import com.borshevskiy.kinopoisktestapp.data.database.FilmDbModel
import com.borshevskiy.kinopoisktestapp.data.network.model.Film
import com.borshevskiy.kinopoisktestapp.data.network.model.FilmInfoDto
import com.borshevskiy.kinopoisktestapp.domain.FilmInfo
import javax.inject.Inject

class FilmMapper @Inject constructor() {

    /** Маппинг ДтоМодели в ДомейнМодель **/

    private fun mapDtoModelToFilmInfo(film: Film) = with(film) {
        FilmInfo(id, name ?: alternativeName ?: "", year ?: 0, description ?: "",
            rating.kp, poster?.url ?: "")
    }

    fun mapDtoToFilmInfo(dto: FilmInfoDto) = dto.films.map { mapDtoModelToFilmInfo(it) }

    /** Маппинг ДтоМодели в МодельБазыДанныхРум **/

    private fun mapDtoModelToFilmDbModel(film: Film) = with(film) {
        FilmDbModel(id, name ?: alternativeName ?: "", year ?: 0, description ?: "",
            rating.kp, poster?.url ?: "")
    }

    fun mapDtoToFilmDbModel(dto: FilmInfoDto) = dto.films.map { mapDtoModelToFilmDbModel(it) }

    /** Маппинг МоделиБазыДанныхРум в ДомейнМодель **/

    private fun mapDbModelToFilmInfo(filmDbModel: FilmDbModel) = with(filmDbModel) {
        FilmInfo(id, name, year, description, rating, imageUrl)
    }

    fun mapListDbModelToFilmInfo(listDbModel: List<FilmDbModel>) = listDbModel.map { mapDbModelToFilmInfo(it) }

}