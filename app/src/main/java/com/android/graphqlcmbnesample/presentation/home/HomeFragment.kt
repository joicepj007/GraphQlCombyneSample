package com.android.graphqlcmbnesample.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.graphqlcmbnesample.R
import com.android.graphqlcmbnesample.databinding.FragmentHomeScreenBinding
import com.android.graphqlcmbnesample.presentation.OnHomeButtonClickCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private var toolbar: Toolbar? = null
    private var mCallback: OnHomeButtonClickCallback? = null
    private val viewModel by viewModels<HomeViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnHomeButtonClickCallback) {
            mCallback = context
        } else throw ClassCastException(context.toString() + "must implement OnHomeButtonClickCallback!")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false)
        this.binding.lifecycleOwner = this
        binding.homeViewModel = viewModel

        toolbar = binding.root.findViewById(R.id.toolbar) as Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar?.title = getString(R.string.str_action_bar_title)

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.addTvShowsClicked.observe(
            viewLifecycleOwner,
            { shouldClicked ->
                if (shouldClicked) {
                    mCallback?.navigateToAddMoviePage()
                }
            }
        )

        viewModel.tvShowsListClicked.observe(
            viewLifecycleOwner,
            { shouldClicked ->
                if (shouldClicked) {
                    mCallback?.navigateToMoviesListPage()
                }
            }
        )


    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }


    companion object {

        val FRAGMENT_NAME: String = HomeFragment::class.java.name

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
