package com.android.graphqlcmbnesample.domain.repository

import com.android.graphqlcmbnesample.GetTvShowsQuery
import com.apollographql.apollo.api.Response

/**
 * To make an interaction between [TvShowsListRepository] & [GetTvShowsListUseCase]
 * */
interface TvShowsListRepository {

    suspend fun queryTvShowsList(): Response<GetTvShowsQuery.Data>
}