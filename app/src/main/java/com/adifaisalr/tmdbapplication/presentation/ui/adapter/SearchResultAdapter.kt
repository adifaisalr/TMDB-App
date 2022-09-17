package com.adifaisalr.tmdbapplication.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.databinding.ItemSearchBinding
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.bumptech.glide.Glide

/**
 * Adapter for the search result list.
 */
class SearchResultAdapter(
    private var users: ArrayList<SearchItem> = arrayListOf(),
    private val actionClickListener: ((SearchItem) -> Unit)? = null
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = users[position]

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemSearchBinding.inflate(inflater, parent, false))
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

    override fun getItemCount(): Int = users.size
}
