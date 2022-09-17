package com.adifaisalr.tmdbapplication.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.databinding.FragmentMediaDetailBinding
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.Review
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.presentation.ui.MainViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.adapter.ReviewAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaDetailFragment : Fragment() {

    private val viewModel by viewModels<MediaDetailViewModel>()
    val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentMediaDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val args: MediaDetailFragmentArgs by navArgs()

    var reviews = arrayListOf<Review>()
    lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mediaId = args.mediaId
        viewModel.mediaType = args.mediaType
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        mainViewModel.updateBottomNav(false)
        initRecyclerView()
        viewModel.mediaDetailResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataHolder.Loading -> {
                    setLoading(true)
                }
                is DataHolder.Success -> {
                    setLoading(false)
                    result.data?.let { setContent(it) }
                }
                else -> {
                    setLoading(false)
                }
            }
        }
        viewModel.mediaReviewResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataHolder.Loading -> {
                    setReviewLoading(true)
                }
                is DataHolder.Success -> {
                    setReviewLoading(false)
                    reviews.clear()
                    if (result.data?.reviews.isNullOrEmpty()) {
                        binding.noReviewLabel.visibility = View.VISIBLE
                        binding.seeAllText.visibility = View.GONE
                    } else {
                        result.data?.reviews?.let {
                            reviews.addAll(it.take(1))
                            binding.noReviewLabel.visibility = View.GONE
                            binding.seeAllText.visibility = View.VISIBLE
                        }
                    }
                    reviewAdapter.notifyDataSetChanged()
                }
                else -> {
                    setReviewLoading(false)
                }
            }
        }
        viewModel.getMovieDetail()
        viewModel.getMovieReviews()
    }

    private fun initRecyclerView() {
        reviewAdapter = ReviewAdapter(reviews)
        binding.reviewRv.adapter = reviewAdapter
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.nestedScrollView.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.nestedScrollView.visibility = View.VISIBLE
        }
    }

    private fun setReviewLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.reviewShimmerLayout.startShimmer()
            binding.reviewShimmerLayout.visibility = View.VISIBLE
        } else {
            binding.reviewShimmerLayout.stopShimmer()
            binding.reviewShimmerLayout.visibility = View.GONE
        }
    }

    private fun setContent(media: Media) {
        Glide.with(binding.movieImage)
            .load(Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W1280 + media.backdropPath)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.movieImage)
        binding.overviewText.text = media.overview
        binding.movieRateScore.text = String.format("%.1f", media.rating)
        binding.releaseDateText.text = media.releaseDate
        binding.seeAllText.setOnClickListener {
            reviews.clear()
            reviews.addAll(viewModel.getReviews())
            reviewAdapter.notifyDataSetChanged()
            binding.seeAllText.visibility = View.GONE
        }
        mainViewModel.updateActionBarTitle(media.title)
        mainViewModel.updateActionBarNavIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back))
    }
}