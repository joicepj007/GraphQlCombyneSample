package com.android.graphqlcmbnesample.presentation.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.graphqlcmbnesample.GetTvShowsQuery
import com.android.graphqlcmbnesample.R
import com.android.graphqlcmbnesample.databinding.FragmentMoviesListBinding
import com.android.graphqlcmbnesample.util.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TvShowsListFragment : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var adapter: TvShowsListAdapter? = null
    private var toolbar: Toolbar? = null
    private val viewModel by viewModels<TvShowsListViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TvShowsListAdapter()
        viewModel.queryMoviesList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movies_list, container, false)
        binding.moviesListViewModel = viewModel
        binding.brandRecyclerView.adapter = adapter

        toolbar = binding.root.findViewById(R.id.toolbar) as Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar?.title = getString(R.string.str_action_bar_title)

        //** Set the Layout Manager of the RecyclerView
        setRVLayoutManager()

        setupObservers()

        return binding.root
    }


    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(requireContext())
        binding.brandRecyclerView.layoutManager = mLayoutManager
        binding.brandRecyclerView.setHasFixedSize(true)
        binding.brandRecyclerView.itemAnimator = null
    }

    private fun setupObservers() {
        viewModel.tvShowsList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ViewState.Loading -> {
                    viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                        load?.let { visibility ->
                            binding.progressBar.visibility =
                                if (visibility) View.GONE else View.VISIBLE
                            binding.brandRecyclerView.visibility =
                                View.VISIBLE
                            binding.noNetwork.visibility = View.GONE
                            binding.textinputError.visibility = View.GONE
                            binding.retry.visibility = View.GONE
                        }
                    })
                }
                is ViewState.Success -> {
                    if (response.value?.data?.movies?.edges?.size != 0) {
                        response.value?.data?.movies?.edges?.let { edges->
                            initRecyclerView(edges)
                        }

                    }
                }
                is ViewState.Error -> {
                    if (response.message.equals(getString(R.string.str_no_internet))) {
                        viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                            load?.let { visibility ->
                                binding.progressBar.visibility =
                                    if (visibility) View.GONE else View.VISIBLE
                                binding.retry.visibility = View.VISIBLE
                                binding.brandRecyclerView.visibility =
                                    View.GONE
                                binding.noNetwork.visibility = View.VISIBLE
                                binding.textinputError.visibility = View.GONE
                            }
                        })
                    } else {
                        viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                            load?.let { visibility ->
                                binding.progressBar.visibility =
                                    if (visibility) View.GONE else View.VISIBLE
                                binding.retry.visibility = View.VISIBLE
                                binding.textinputError.visibility =
                                    View.VISIBLE
                                binding.brandRecyclerView.visibility =
                                    View.GONE
                                binding.noNetwork.visibility = View.GONE
                                binding.textinputError.text =
                                    response.message.toString()
                            }
                        })
                    }
                }
            }
        }

        viewModel.retryClickedLiveData.observe(viewLifecycleOwner, {
            it?.let { clicked ->
                if (clicked) {
                    if (binding.retry.isVisible) {
                        viewModel.queryMoviesList()
                    }
                }
            }
        })
    }

    private fun initRecyclerView(edge: List<GetTvShowsQuery.Edge?>) {
        adapter?.updateData(edge)
    }

    companion object {

        val FRAGMENT_NAME: String = TvShowsListFragment::class.java.name

        @JvmStatic
        fun newInstance() =
            TvShowsListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


}