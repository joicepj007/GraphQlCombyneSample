package com.android.graphqlcmbnesample.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.graphqlcmbnesample.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel@Inject constructor() : ViewModel() {

    private val _addTvShowsClicked = SingleLiveEvent<Boolean>()
    val addTvShowsClicked: LiveData<Boolean> = _addTvShowsClicked


    private val _tvShowsListClicked = SingleLiveEvent<Boolean>()
    val tvShowsListClicked: LiveData<Boolean> = _tvShowsListClicked


    fun onAddMoviesButtonClicked() {
        _addTvShowsClicked.value = true
    }
    fun onMoviesListButtonClicked() {
        _tvShowsListClicked.value = true
    }


}
