package com.adifaisalr.tmdbapplication.presentation.ui.media

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.adifaisalr.tmdbapplication.databinding.FragmentMediaBinding
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.presentation.ui.adapter.HomeBannerAdapter
import com.adifaisalr.tmdbapplication.presentation.ui.adapter.HomeCarouselAdapter
import com.adifaisalr.tmdbapplication.presentation.ui.home.HomeFragmentDirections
import com.adifaisalr.tmdbapplication.presentation.util.NavigationUtils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable

@AndroidEntryPoint
class MediaFragment : Fragment() {

    private var currentPage: Int = 0
    private var _binding: FragmentMediaBinding? = null

    private val viewModel by viewModels<MediaViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var popularMedia = arrayListOf<Media>()
    lateinit var popularAdapter: HomeBannerAdapter

    var trendingMedia = arrayListOf<Media>()
    lateinit var trendingAdapter: HomeCarouselAdapter

    var discoverMedia = arrayListOf<Media>()
    lateinit var discoverAdapter: HomeCarouselAdapter

    val handler = Handler(Looper.getMainLooper())
    lateinit var taskRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(ARG_MEDIA_TYPE) }?.apply {
            val mediaType = MediaViewModel.Companion.MediaType.values().find { it.id == getInt(ARG_MEDIA_TYPE) }
            mediaType?.let { viewModel.mediaType = it }
        }
        initRecyclerView()
        observeViewModel()
        viewModel.getPopularMovies()
        viewModel.getTrendingMovies()
        viewModel.getDiscoverMovies()
    }

    override fun onPause() {
        handler.removeCallbacks(taskRunnable)
        Log.d("job", "cancel")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        setAutoSlide()
        Log.d("job", "resume")
    }

    private fun setAutoSlide() {
        taskRunnable = Runnable {
            if (currentPage == popularMedia.size) {
                currentPage = 0
            }

            //The second parameter ensures smooth scrolling
            _binding?.bannerViewPager?.setCurrentItem(currentPage++, true)
            handler.postDelayed(taskRunnable, 3500)
        }
        handler.removeCallbacks(taskRunnable)
        taskRunnable.run()
    }

    private fun initRecyclerView() {
        val actionClickListener: ((Media) -> Unit) = { media ->
            val action = HomeFragmentDirections.actionGlobalMediaDetailFragment(media.id, viewModel.mediaType)
            findNavController().safeNavigate(action)
        }
        popularAdapter = HomeBannerAdapter(popularMedia, actionClickListener)
        binding.bannerViewPager.adapter = popularAdapter
        binding.bannerViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPage = position
                }
            }
        )
        trendingAdapter = HomeCarouselAdapter(trendingMedia, actionClickListener)
        binding.trendingRV.adapter = trendingAdapter
        discoverAdapter = HomeCarouselAdapter(discoverMedia, actionClickListener)
        binding.discoverRV.adapter = discoverAdapter
    }

    private fun observeViewModel() {
        viewModel.popularMovieResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataHolder.Loading -> {
                }
                is DataHolder.Success -> {
                    if (result.data?.results.isNullOrEmpty()) {
                        popularMedia.clear()
                        popularAdapter.notifyDataSetChanged()
                    } else {
                        result.data?.results?.let {
                            popularMedia.clear()
                            popularMedia.addAll(it.take(5))
                            popularAdapter.notifyDataSetChanged()
                        }
                    }
                    binding.trendingRV.visibility = View.VISIBLE
                    setAutoSlide()
                }
                else -> {
                }
            }

        }
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

    companion object {
        const val ARG_MEDIA_TYPE = "media_type"
        const val MEDIA_TYPE_MOVIE = 1
        const val MEDIA_TYPE_TV = 2
    }
}