package com.github.snuffix.koleo.di

import com.github.snuffix.domain.repository.StationsRepository
import com.github.snuffix.domain.repository.StationsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindStationsRepository(repository: StationsRepositoryImpl): StationsRepository
}