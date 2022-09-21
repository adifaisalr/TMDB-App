package com.adifaisalr.tmdbapplication.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.databinding.ItemSearchBinding
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.bumptech.glide.Glide

class SearchResultPagingAdapter(
    private val actionClickListener: ((SearchItem) -> Unit)? = null
) : PagingDataAdapter<SearchItem, SearchResultPagingAdapter.ViewHolder>(PHOTO_COMPARATOR) {

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultPagingAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemSearchBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: SearchResultPagingAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { photo ->
            holder.bind(photo)
        }
    }

    inner class ViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchItem) {
            binding.searchItem = item
            Glide.with(binding.image)
                .load(Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W92 + item.posterPath)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.image)
            actionClickListener?.let { listener ->
                binding.root.setOnClickListener {
                    listener.invoke(item)
                }
            }
            binding.executePendingBindings()
        }
    }
}
