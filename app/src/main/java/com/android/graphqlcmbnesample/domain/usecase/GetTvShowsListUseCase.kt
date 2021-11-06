package com.android.graphqlcmbnesample.domain.usecase

import com.android.graphqlcmbnesample.GetTvShowsQuery
import com.android.graphqlcmbnesample.domain.repository.TvShowsListRepository
import com.apollographql.apollo.api.Response
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of [TvShowsListViewModel](which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetTvShowsListUseCase @Inject constructor(private val repository: TvShowsListRepository) {

    suspend fun getTvShowsListData(): Response<GetTvShowsQuery.Data> {
        return repository.queryTvShowsList()
    }
}