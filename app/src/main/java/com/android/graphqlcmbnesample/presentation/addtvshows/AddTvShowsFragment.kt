package com.android.graphqlcmbnesample.presentation.addtvshows

import android.app.DatePickerDialog
import android.content.Context
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
import com.android.graphqlcmbnesample.R
import com.android.graphqlcmbnesample.databinding.FragmentAddMovieBinding
import com.android.graphqlcmbnesample.presentation.OnHomeButtonClickCallback
import com.android.graphqlcmbnesample.util.PATTERN_SERVER_DATE_TIME
import com.android.graphqlcmbnesample.util.PATTERN_START_WITH_MONTH
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.android.graphqlcmbnesample.util.ViewState
import com.android.graphqlcmbnesample.util.convertDateString

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AddTvShowsFragment : Fragment() {

    private lateinit var binding: FragmentAddMovieBinding
    private var toolbar: Toolbar? = null
    private var mCallback: OnHomeButtonClickCallback? = null
    private val viewModel by viewModels<AddTvShowsViewModel>()
    private var isAllFieldsChecked = false
    private var cal = Calendar.getInstance()
    private var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var dateFormat: Date? = null

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_movie, container, false)
        binding.addMovieViewModel = viewModel

        toolbar = binding.root.findViewById(R.id.toolbar) as Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar?.title = getString(R.string.str_action_bar_title)


        setupObservers()
        dateTimePicker()
        onClick()
        return binding.root
    }

    private fun dateTimePicker() {
        // create an OnDateSetListener
        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
    }

    private fun updateDateInView() {
        val sdf = SimpleDateFormat(PATTERN_SERVER_DATE_TIME, Locale.US)
        val formatteDate = convertDateString(
            PATTERN_SERVER_DATE_TIME,
            PATTERN_START_WITH_MONTH,
            sdf.format(cal.time)
        )

        binding.tvReleasedate.setText(formatteDate)
        dateFormat = cal.time
    }

    private fun onClick() {
        binding.tvReleasedate.setOnClickListener {

            DatePickerDialog(
                requireContext(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
            ).show()
        }
    }

    private fun setupObservers() {
        viewModel.addMovieClicked.observe(viewLifecycleOwner, {
            it?.let { clicked ->
                if (clicked) {
                    // whether the entered data is valid or if any fields are left blank.
                    isAllFieldsChecked = CheckAllFields()
                    if (isAllFieldsChecked) {
                        dateFormat?.let { date ->
                            viewModel.addToMovieList(
                                binding.tvTvshow.text.toString(),
                                date, binding.tvSeasons.text.toString().toDouble()
                            )
                        }
                    }
                }
            }
        })

        viewModel.addToMovieList.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ViewState.Loading -> {
                    viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                        load?.let { visibility ->
                            binding.progressBar.visibility =
                                if (visibility) View.GONE else View.VISIBLE
                        }
                    })
                }
                is ViewState.Success -> {
                    parentFragmentManager.popBackStack()
                }
                is ViewState.Error -> {
                    if (response.message.equals(getString(R.string.str_no_internet))) {
                        viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                            load?.let { visibility ->
                                binding.progressBar.visibility =
                                    if (visibility) View.GONE else View.VISIBLE
                                binding.retry.visibility = View.VISIBLE
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
                                binding.noNetwork.visibility = View.GONE
                                binding.textinputError.text =
                                    response.message.toString()
                            }
                        })
                    }
                }
            }

        })

        viewModel.retryClickedLiveData.observe(viewLifecycleOwner, {
            it?.let { clicked ->
                if (clicked) {
                    if (binding.retry.isVisible) {
                        isAllFieldsChecked = CheckAllFields()
                        if (isAllFieldsChecked) {
                            dateFormat?.let { date ->
                                viewModel.addToMovieList(
                                    binding.tvTvshow.text.toString(),
                                    date, binding.tvSeasons.text.toString().toDouble()
                                )
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    companion object {

        val FRAGMENT_NAME: String = AddTvShowsFragment::class.java.name

        @JvmStatic
        fun newInstance() =
            AddTvShowsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    // function which checks all the text fields
    // are filled or not by the user.
    // when user clicks on the Save button
    // this function is triggered.
    private fun CheckAllFields(): Boolean {
        if (binding.tvTvshow.length() == 0) {
            binding.tvTvshow.error = getString(R.string.str_tv_show)
            return false
        }
        if (binding.tvReleasedate.length() == 0) {
            binding.tvReleasedate.error = getString(R.string.str_releasedate)
            return false
        }
        if (binding.tvSeasons.length() == 0) {
            binding.tvSeasons.error = getString(R.string.str_seasons)
            return false
        }

        // after all validation return true.
        return true
    }
}
