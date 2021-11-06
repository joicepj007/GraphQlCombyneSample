package com.android.graphqlcmbnesample.di

import com.android.graphqlcmbnesample.data.CombyneApi
import com.android.graphqlcmbnesample.data.repository.AddTvShowsRepositoryImpl
import com.android.graphqlcmbnesample.data.repository.TvShowsListRepositoryImpl
import com.android.graphqlcmbnesample.domain.repository.AddTvShowsRepository
import com.android.graphqlcmbnesample.domain.repository.TvShowsListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideWebService() = CombyneApi()

    @Singleton
    @Provides
    fun provideBrandListRepository(
        webService: CombyneApi
    ): AddTvShowsRepository {
        return AddTvShowsRepositoryImpl(webService)
    }

    @Singleton
    @Provides
    fun provideIssuesListRepository(
        webService: CombyneApi
    ): TvShowsListRepository {
        return TvShowsListRepositoryImpl(webService)
    }

}