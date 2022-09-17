package com.adifaisalr.tmdbapplication.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.databinding.FragmentHomeBinding
import com.adifaisalr.tmdbapplication.presentation.ui.MainViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.adapter.HomeFragmentAdapter
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var fragmentAdapter: HomeFragmentAdapter
    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAdapter = HomeFragmentAdapter(this)
        binding.viewPager2.adapter = fragmentAdapter
        binding.viewPager2.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            val mediaType = MediaViewModel.Companion.MediaType.values().find { it.id == position }
            mediaType?.let { tab.text = getString(it.titleStringId) }
        }.attach()
        mainViewModel.updateActionBarTitle(getString(R.string.app_name))
        mainViewModel.updateActionBarNavIcon(null)
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}