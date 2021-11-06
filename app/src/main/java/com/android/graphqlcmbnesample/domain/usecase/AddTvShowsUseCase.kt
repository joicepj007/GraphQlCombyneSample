package com.android.graphqlcmbnesample.domain.usecase

import com.android.graphqlcmbnesample.AddTvShowsMutation
import com.android.graphqlcmbnesample.domain.repository.AddTvShowsRepository
import com.apollographql.apollo.api.Response
import java.util.Date
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of [AddTvShowsViewModel](which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class AddTvShowsUseCase @Inject constructor(private val repository: AddTvShowsRepository) {

    suspend fun addMoviesData(title:String,releaseDate: Date,seasons:Double): Response<AddTvShowsMutation.Data> {
        return repository.addMovieList(title, releaseDate, seasons)
    }
}