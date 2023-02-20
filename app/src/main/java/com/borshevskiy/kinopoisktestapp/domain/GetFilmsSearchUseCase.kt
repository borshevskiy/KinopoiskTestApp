package com.borshevskiy.kinopoisktestapp.domain

import javax.inject.Inject

/** Юзкейс на получение поиска фильма из АПИ по имени **/

class GetFilmsSearchUseCase @Inject constructor(private val repository: FilmRepository) {

    suspend operator fun invoke(sortQuery: String, searchQuery: String) = repository.searchFilms(sortQuery, searchQuery)
}