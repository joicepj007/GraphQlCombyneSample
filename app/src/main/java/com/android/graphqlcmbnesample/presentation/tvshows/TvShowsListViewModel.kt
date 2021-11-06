package com.android.graphqlcmbnesample.presentation.tvshows

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.graphqlcmbnesample.GetTvShowsQuery
import com.android.graphqlcmbnesample.domain.usecase.GetTvShowsListUseCase
import com.android.graphqlcmbnesample.util.SingleLiveEvent
import com.android.graphqlcmbnesample.util.ViewState
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TvShowsListViewModel  @Inject constructor(private val getTvShowsListUseCase: GetTvShowsListUseCase) : ViewModel() {

    private val _tvShowsList by lazy { SingleLiveEvent<ViewState<Response<GetTvShowsQuery.Data>>>() }
    val tvShowsList: LiveData<ViewState<Response<GetTvShowsQuery.Data>>>
        get() = _tvShowsList

    val isLoad = MutableLiveData<Boolean>()
    val retryClickedLiveData = MutableLiveData<Boolean>()

    init {
        isLoad.value = false
    }

    fun queryMoviesList() = viewModelScope.launch {
        _tvShowsList.postValue(ViewState.Loading())
        try {
            val response = getTvShowsListUseCase.getTvShowsListData()
            isLoad.value = true
            _tvShowsList.postValue(ViewState.Success(response))
        } catch (e: ApolloException) {
            Log.d("ApolloException******xyz*******", "Failure", e)
            isLoad.value = true
            _tvShowsList.postValue(ViewState.Error("Error fetching characters"))
        }
    }

    fun onButtonRetryClicked() {
        isLoad.value = false
        retryClickedLiveData.value = true
    }


}