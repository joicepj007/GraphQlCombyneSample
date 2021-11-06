package com.android.graphqlcmbnesample.data.repository
import com.android.graphqlcmbnesample.AddTvShowsMutation
import com.android.graphqlcmbnesample.data.CombyneApi
import com.android.graphqlcmbnesample.domain.repository.AddTvShowsRepository
import com.android.graphqlcmbnesample.type.CreateMovieFieldsInput
import com.android.graphqlcmbnesample.type.CreateMovieInput
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import java.util.Date
import javax.inject.Inject

/**
 * This repository is responsible for
 * adding data[AddTvShowsMutation.Data()] to server
 * */
class AddTvShowsRepositoryImpl @Inject constructor(
    private val webService: CombyneApi
) : AddTvShowsRepository {
    override suspend fun addMovieList(
        title: String,
        releaseDate: Date,
        seasons: Double
    ): Response<AddTvShowsMutation.Data> {
        val mutation = AddTvShowsMutation(
            input = CreateMovieInput(
                fields =
                Input.fromNullable(
                    CreateMovieFieldsInput(
                        title = title,
                        releaseDate = Input.fromNullable(releaseDate),
                        seasons = Input.fromNullable(seasons)
                    )
                )
            )
        )
        return webService.getApolloClient().mutate(mutation).await()
    }
}