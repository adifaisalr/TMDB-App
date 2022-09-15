package com.adifaisalr.tmdbapplication.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.databinding.ItemSliderBinding
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.bumptech.glide.Glide

/**
 * Adapter for the home slider list.
 */
class HomeSliderAdapter(
    private var users: ArrayList<Media> = arrayListOf()
) : RecyclerView.Adapter<HomeSliderAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = users[position]

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Media) {
            binding.movie = item
            Glide.with(binding.imageView)
                .load(Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W92 + item.posterPath)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imageView)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSliderBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = users.size
}
