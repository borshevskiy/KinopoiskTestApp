package com.borshevskiy.kinopoisktestapp.domain

import javax.inject.Inject

/** Юзкейс на получение всего списка фильмов из АПИ **/

class GetFilmsListUseCase @Inject constructor(private val repository: FilmRepository) {

    suspend operator fun invoke(sortQuery: String) = repository.getFilmsList(sortQuery)
}