package com.android.graphqlcmbnesample.presentation.tvshows


import androidx.recyclerview.widget.DiffUtil
import com.android.graphqlcmbnesample.GetTvShowsQuery

class TvShowsDiffCallBack(
    private val newItems: List<GetTvShowsQuery.Edge?>,
    private val oldItems: List<GetTvShowsQuery.Edge?>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return (oldItem?.node?.id == newItem?.node?.id) && (oldItem?.node?.title == newItem?.node?.title)
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }
}