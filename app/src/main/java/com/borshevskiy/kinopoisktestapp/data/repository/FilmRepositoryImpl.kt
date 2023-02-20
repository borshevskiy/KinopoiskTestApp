package com.borshevskiy.kinopoisktestapp.data.repository

import com.borshevskiy.kinopoisktestapp.data.database.FilmsDao
import com.borshevskiy.kinopoisktestapp.data.mapper.FilmMapper
import com.borshevskiy.kinopoisktestapp.data.network.ApiService
import com.borshevskiy.kinopoisktestapp.domain.FilmInfo
import com.borshevskiy.kinopoisktestapp.domain.FilmRepository
import com.borshevskiy.kinopoisktestapp.domain.util.NetworkResult
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val filmsDao: FilmsDao,
    private val mapper: FilmMapper
) : FilmRepository {

    /** Получаю весь список фильмов из Api запроса **/

    override suspend fun getFilmsList(sortQuery: String): NetworkResult<List<FilmInfo>> {
        apiService.getAllFilms(sortQuery).let {
            return when {
                it.isSuccessful -> {
                    /** Очищаю БД перед загрузкой новых фильмов, т.к. подгружаются каждый раз новые **/
                    filmsDao.deleteAll()
                    filmsDao.insertFilms(mapper.mapDtoToFilmDbModel(it.body()!!))
                    NetworkResult.Success(mapper.mapDtoToFilmInfo(it.body()!!))
                }
                else -> NetworkResult.Error(it.message())
            }
        }
    }

    /** Получаю список фильмов из поискового Api запроса **/

    override suspend fun searchFilms(sortQuery: String, searchQuery: String): NetworkResult<List<FilmInfo>> {
        apiService.getFilmBySearchedName(sortType = sortQuery, search = searchQuery).let {
            return when {
                it.isSuccessful -> NetworkResult.Success(mapper.mapDtoToFilmInfo(it.body()!!))
                else -> NetworkResult.Error(it.message())
            }
        }
    }

    /** Получаю кэш в зависимости от порядка сортировки: по возрастанию года или по убыванию **/

    override suspend fun getDBCache(sortQuery: String): List<FilmInfo> {
        return when (sortQuery) {
            "1" -> mapper.mapListDbModelToFilmInfo(filmsDao.readFilmsYearAsc())
            else -> mapper.mapListDbModelToFilmInfo(filmsDao.readFilmsYearDesc())
        }
    }
}