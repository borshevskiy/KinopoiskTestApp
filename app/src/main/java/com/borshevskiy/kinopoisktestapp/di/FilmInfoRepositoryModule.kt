package com.borshevskiy.kinopoisktestapp.di

import com.borshevskiy.kinopoisktestapp.data.repository.FilmRepositoryImpl
import com.borshevskiy.kinopoisktestapp.domain.FilmRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FilmInfoRepositoryModule {

    @Binds
    abstract fun bindRepository(impl: FilmRepositoryImpl): FilmRepository
}