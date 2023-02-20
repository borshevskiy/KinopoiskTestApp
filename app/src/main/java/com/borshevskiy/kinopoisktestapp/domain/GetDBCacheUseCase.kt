package com.borshevskiy.kinopoisktestapp.domain

import javax.inject.Inject

/** Юзкейс на получение кэша из БД **/

class GetDBCacheUseCase @Inject constructor(private val repository: FilmRepository) {

    suspend operator fun invoke(sortQuery: String) = repository.getDBCache(sortQuery)
}