package com.adifaisalr.tmdbapplication.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.databinding.ItemReviewBinding
import com.adifaisalr.tmdbapplication.domain.model.Review
import com.bumptech.glide.Glide

/**
 * Adapter for the review list.
 */
class ReviewAdapter(
    private var reviews: ArrayList<Review> = arrayListOf(),
    private val actionClickListener: ((Review) -> Unit)? = null
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = reviews[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemReviewBinding.inflate(inflater, parent, false))
    }

    inner class ViewHolder(val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) {
            item.authorDetails.avatarPath?.let { path ->
                Glide.with(binding.profileImage)
                    .load(Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W92 + path)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.profileImage)
            }
            actionClickListener?.let { listener ->
                binding.root.setOnClickListener {
                    listener.invoke(item)
                }
            }
            binding.profileNameText.text = item.author
            binding.reviewText.text = item.content
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = reviews.size
}
