package com.borshevskiy.kinopoisktestapp.di

import com.borshevskiy.kinopoisktestapp.BuildConfig
import com.borshevskiy.kinopoisktestapp.data.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    /** Повторяющиеся поля передал в интерцептор, апиключ и урл получаю из билдконфига **/

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor = Interceptor {
        val request = it.request()
        val newHttpUrl = request.url.newBuilder()
            .addQueryParameter("token", BuildConfig.API_KEY)
            .addQueryParameter("sortField", "year")
            .addQueryParameter("limit", "100").toString()
        val actualRequest = request.newBuilder().url(newHttpUrl).build()
        it.proceed(actualRequest)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(client: OkHttpClient): ApiService =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(ApiService::class.java)

}