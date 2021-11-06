package com.android.graphqlcmbnesample.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.graphqlcmbnesample.R
import com.android.graphqlcmbnesample.presentation.addtvshows.AddTvShowsFragment
import com.android.graphqlcmbnesample.presentation.home.HomeFragment
import com.android.graphqlcmbnesample.presentation.tvshows.TvShowsListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CombyneMainActivity : AppCompatActivity(), OnHomeButtonClickCallback{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToUserListPage()
    }


    private fun navigateToUserListPage() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_container,
                HomeFragment.newInstance(),
                HomeFragment.FRAGMENT_NAME
            )
            .commit()
    }

    override fun navigateToAddMoviePage() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_container,
                AddTvShowsFragment.newInstance(),
                AddTvShowsFragment.FRAGMENT_NAME
            )
            .addToBackStack(AddTvShowsFragment.FRAGMENT_NAME)
            .commit()
    }

    override fun navigateToMoviesListPage() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_container,
                TvShowsListFragment.newInstance(),
                TvShowsListFragment.FRAGMENT_NAME
            )
            .addToBackStack(TvShowsListFragment.FRAGMENT_NAME)
            .commitAllowingStateLoss()

    }

}