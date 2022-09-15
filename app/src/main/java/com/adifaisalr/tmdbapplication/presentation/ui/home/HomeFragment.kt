package com.adifaisalr.tmdbapplication.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adifaisalr.tmdbapplication.databinding.FragmentHomeBinding
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.presentation.ui.adapter.HomeSliderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val viewModel by viewModels<HomeViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var trendingMedia = arrayListOf<Media>()
    lateinit var trendingAdapter: HomeSliderAdapter

    var discoverMedia = arrayListOf<Media>()
    lateinit var discoverAdapter: HomeSliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeViewModel()
        viewModel.getTrendingMovies()
        viewModel.getDiscoverMovies()
    }

    private fun initRecyclerView() {
        trendingAdapter = HomeSliderAdapter(trendingMedia)
        binding.trendingRV.adapter = trendingAdapter
        discoverAdapter = HomeSliderAdapter(discoverMedia)
        binding.discoverRV.adapter = discoverAdapter
    }

    private fun observeViewModel() {
        viewModel.trendingMovieResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataHolder.Loading -> {
                    setTrendingLoading(true)
                }
                is DataHolder.Success -> {
                    setTrendingLoading(false)
                    if (result.data?.results.isNullOrEmpty()) {
                        trendingMedia.clear()
                        trendingAdapter.notifyDataSetChanged()
                    } else {
                        result.data?.results?.let {
                            trendingMedia.clear()
                            trendingMedia.addAll(it)
                            trendingAdapter.notifyDataSetChanged()
                        }
                    }
                    binding.trendingRV.visibility = View.VISIBLE
                }
                else -> {
                    setTrendingLoading(false)
                }
            }
        }
        viewModel.discoverMovieResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataHolder.Loading -> {
                    setDiscoverLoading(true)
                }
                is DataHolder.Success -> {
                    setDiscoverLoading(false)
                    if (result.data?.results.isNullOrEmpty()) {
                        discoverMedia.clear()
                        discoverAdapter.notifyDataSetChanged()
                    } else {
                        result.data?.results?.let {
                            discoverMedia.clear()
                            discoverMedia.addAll(it)
                            discoverAdapter.notifyDataSetChanged()
                        }
                    }
                    binding.discoverRV.visibility = View.VISIBLE
                }
                else -> {
                    setDiscoverLoading(false)
                }
            }
        }
    }

    private fun setTrendingLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.trendingShimmerLayout.startShimmer()
            binding.trendingShimmerLayout.visibility = View.VISIBLE
        } else {
            binding.trendingShimmerLayout.stopShimmer()
            binding.trendingShimmerLayout.visibility = View.GONE
        }
    }

    private fun setDiscoverLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.discoverhimmerLayout.startShimmer()
            binding.discoverhimmerLayout.visibility = View.VISIBLE
        } else {
            binding.discoverhimmerLayout.stopShimmer()
            binding.discoverhimmerLayout.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}