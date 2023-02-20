package com.borshevskiy.kinopoisktestapp.presentation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.borshevskiy.kinopoisktestapp.domain.FilmInfo
import com.borshevskiy.kinopoisktestapp.domain.GetDBCacheUseCase
import com.borshevskiy.kinopoisktestapp.domain.GetFilmsListUseCase
import com.borshevskiy.kinopoisktestapp.domain.GetFilmsSearchUseCase
import com.borshevskiy.kinopoisktestapp.domain.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFilmsListUseCase: GetFilmsListUseCase,
    private val getFilmsSearchUseCase: GetFilmsSearchUseCase,
    private val getDBCacheUseCase: GetDBCacheUseCase,
    application: Application
): AndroidViewModel(application) {

    /** переменная сортировки **/

    val orderQuery = MutableLiveData("1")

    /** ROOM получение кэша из бд **/

    private val _dbFilmList = MutableLiveData<List<FilmInfo>>()
    val dbFilmList: LiveData<List<FilmInfo>> get() = _dbFilmList

    fun readDatabase(sortQuery: String) = viewModelScope.launch {
        _dbFilmList.value = getDBCacheUseCase(sortQuery)!!
    }

    /** RETROFIT: две переменных 1)весь список фильмов 2)фильмы поискового запроса
     * две функции получения общего списка фильмов и поиска с предварительной проверкой на интернет-соединение **/

    private val _filmList = MutableLiveData<NetworkResult<List<FilmInfo>>>()
    val filmList: LiveData<NetworkResult<List<FilmInfo>>> get() = _filmList

    private val _searchedFilmList = MutableLiveData<NetworkResult<List<FilmInfo>>>()
    val searchedFilmList: LiveData<NetworkResult<List<FilmInfo>>> get() = _searchedFilmList

    fun getFilms(sortQuery: String) = viewModelScope.launch {
        getFilmsSafeCall(sortQuery)
    }

    fun searchFilms(sortQuery: String, searchQuery: String) = viewModelScope.launch {
        searchFilmsSafeCall(sortQuery, searchQuery)
    }

    private suspend fun searchFilmsSafeCall(sortQuery: String, searchQuery: String) {
        _searchedFilmList.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                _searchedFilmList.value = getFilmsSearchUseCase(sortQuery, searchQuery)!!
            } catch (e: Exception) {
                _searchedFilmList.value = NetworkResult.Error("Films not found.")
            }
        } else {
            _searchedFilmList.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getFilmsSafeCall(sortQuery: String) {
        _filmList.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                _filmList.value = getFilmsListUseCase(sortQuery)!!
            } catch (e: Exception) {
                _filmList.value = NetworkResult.Error("Films not found.")
            }
        } else _filmList.value = NetworkResult.Error("No Internet Connection.")
    }

    /** Проверка интернет-соединения **/

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}