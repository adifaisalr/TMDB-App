package com.adifaisalr.tmdbapplication.presentation.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaFragment
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaFragment.Companion.ARG_MEDIA_TYPE

class HomeFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = MediaFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_MEDIA_TYPE, position)
        }
        return fragment
    }
}