package com.android.graphqlcmbnesample.domain.repository

import com.android.graphqlcmbnesample.AddTvShowsMutation
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import java.util.*

/**
 * To make an interaction between [AddTvShowsRepository] & [AddTvShowsUseCase]
 * */
interface AddTvShowsRepository {

    suspend fun addMovieList(title:String,releaseDate:Date,seasons:Double): Response<AddTvShowsMutation.Data>

}