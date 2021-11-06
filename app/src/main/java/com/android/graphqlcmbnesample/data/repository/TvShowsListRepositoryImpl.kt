package com.android.graphqlcmbnesample.data.repository
import com.android.graphqlcmbnesample.GetTvShowsQuery
import com.android.graphqlcmbnesample.data.CombyneApi
import com.android.graphqlcmbnesample.domain.repository.TvShowsListRepository
import com.android.graphqlcmbnesample.type.MovieOrder
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await

/**
 * This repository is responsible for
 * fetching data[GetTvShowsQuery.Data()] from server
 * */
class TvShowsListRepositoryImpl(
    private val webService: CombyneApi
) : TvShowsListRepository {
     override suspend fun queryTvShowsList(): Response<GetTvShowsQuery.Data> {
       return webService.getApolloClient().query(GetTvShowsQuery(Input.fromNullable(listOf(MovieOrder.CREATEDAT_DESC)))).await()
   }
}