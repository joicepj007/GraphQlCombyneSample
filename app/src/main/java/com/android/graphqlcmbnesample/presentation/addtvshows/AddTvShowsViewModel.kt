package com.android.graphqlcmbnesample.presentation.addtvshows

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.graphqlcmbnesample.AddTvShowsMutation
import com.android.graphqlcmbnesample.domain.usecase.AddTvShowsUseCase
import com.android.graphqlcmbnesample.util.SingleLiveEvent
import com.android.graphqlcmbnesample.util.ViewState
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AddTvShowsViewModel @Inject constructor(private val addTvShowsUseCase: AddTvShowsUseCase) : ViewModel() {

     private val _addToMovieList by lazy { SingleLiveEvent<ViewState<Response<AddTvShowsMutation.Data>>>() }
  val addToMovieList: LiveData<ViewState<Response<AddTvShowsMutation.Data>>>
      get() = _addToMovieList

    private val _addMovieClicked = SingleLiveEvent<Boolean>()
    val addMovieClicked: LiveData<Boolean> = _addMovieClicked

    val retryClickedLiveData = MutableLiveData<Boolean>()
    val isLoad = MutableLiveData<Boolean>()

    init {
        isLoad.value = true
    }

    fun addToMovieList(title:String,releaseDate: Date,seasons:Double) = viewModelScope.launch {
        _addToMovieList.postValue(ViewState.Loading())
        try {
            val response = addTvShowsUseCase.addMoviesData(title,releaseDate,seasons)
            if (!response.hasErrors()) {
                // Response successful
                isLoad.value = true
                _addToMovieList.postValue(ViewState.Success(response))
                Log.d("Success", "Response: ${response.data}")
            } else {
                // Request was successful but contains errors
                isLoad.value = true
                _addToMovieList.postValue(ViewState.Error(response.errors.toString()))
                Log.d("ApolloException", "Response has errors: ${response.errors}")
            }

        } catch (ae: ApolloException) {
            Log.d("ApolloException", "Failure", ae)
            isLoad.value = true
            _addToMovieList.postValue(ViewState.Error("Error fetching data"))
        }
    }


    fun onSaveButtonClicked() {
        isLoad.value = false
        _addMovieClicked.value = true

    }

    fun onButtonRetryClicked() {
        isLoad.value = false
        retryClickedLiveData.value = true
    }


}
