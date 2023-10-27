package com.tuan.englishforkid.di

import com.tuan.englishforkid.db.DataRemoteAPI
import com.tuan.englishforkid.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object AppModule {

    @Singleton
    @Provides
    fun provideAppRepository(dataRemoteAPI: DataRemoteAPI): Repository =
        Repository(dataRemoteAPI)
}