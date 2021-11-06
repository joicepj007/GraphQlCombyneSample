package com.android.graphqlcmbnesample.presentation.tvshows

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.graphqlcmbnesample.GetTvShowsQuery
import com.android.graphqlcmbnesample.R
import com.android.graphqlcmbnesample.databinding.HolderItemBinding
import com.android.graphqlcmbnesample.util.PATTERN_SERVER_DATE_TIME
import com.android.graphqlcmbnesample.util.PATTERN_START_WITH_MONTH
import com.android.graphqlcmbnesample.util.convertDateString
import kotlinx.android.synthetic.main.fragment_add_movie.view.*
import kotlinx.android.synthetic.main.holder_item.view.*
import java.util.ArrayList

internal class TvShowsListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var tvshowsList: List<GetTvShowsQuery.Edge?> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderAlbumBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), R.layout.holder_item, parent, false
        )
        return EventViewHolder(holderAlbumBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EventViewHolder).onBind(getItem(position), position)
    }

    private fun getItem(position: Int): GetTvShowsQuery.Edge? {
        return tvshowsList[position]
    }

    override fun getItemCount(): Int {
        return tvshowsList.size
    }

    fun updateData(edge: List<GetTvShowsQuery.Edge?>) {
        val diffCallback = TvShowsDiffCallBack(edge, tvshowsList)
        val diffResult =  DiffUtil.calculateDiff(diffCallback)
            tvshowsList = edge.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    inner class EventViewHolder(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(tvshowsInfo: GetTvShowsQuery.Edge?, position: Int) {
            val holderUserBinding = dataBinding as HolderItemBinding
            holderUserBinding.movies = tvshowsInfo
            val formatteDate = convertDateString(
                PATTERN_SERVER_DATE_TIME,
                PATTERN_START_WITH_MONTH,
                tvshowsInfo?.node?.releaseDate.toString()
            )
            itemView.tv_release.text = formatteDate.toString()
        }
    }
}
