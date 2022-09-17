package com.adifaisalr.tmdbapplication.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.databinding.ItemBannerBinding
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.bumptech.glide.Glide

/**
 * Adapter for the home banner slider.
 */
class HomeBannerAdapter(
    private var medias: ArrayList<Media> = arrayListOf(),
    private val actionClickListener: ((Media) -> Unit)? = null
) : RecyclerView.Adapter<HomeBannerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = medias[position]

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemBannerBinding.inflate(inflater, parent, false))
    }

    inner class ViewHolder(val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Media) {
            binding.media = item
            Glide.with(binding.imageView)
                .load(Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W1280 + item.backdropPath)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imageView)
            actionClickListener?.let { listener ->
                binding.root.setOnClickListener {
                    listener.invoke(item)
                }
            }
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = medias.size
}
