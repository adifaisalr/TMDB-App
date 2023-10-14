@file:OptIn(ExperimentalFoundationApi::class)

package com.adifaisalr.tmdbapplication.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.presentation.ui.MainViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaScreen
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MediaHomeScreen(
                    navController = findNavController()
                )
            }
        }
    }

    @Composable
    fun MediaHomeScreen(
        modifier: Modifier = Modifier,
        navController: NavController,
    ) {
        val pagerState = rememberPagerState { 2 }
        val tabList = listOf(
            stringResource(id = R.string.title_movies),
            stringResource(id = R.string.title_tvs)
        )
        val coroutineScope = rememberCoroutineScope()

        Column(modifier = modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabList.forEachIndexed { index, str ->
                    TabBarView(text = str, selected = pagerState.currentPage == index) {
                        coroutineScope.launch {
                            pagerState.scrollToPage(page = index, pageOffsetFraction = 0f)
                        }
                    }
                }
            }
            HorizontalPager(
                modifier = Modifier,
                state = pagerState
            ) { page ->
                val mediaType = MediaViewModel.Companion.MediaType.values().find { it.id == page }
                    ?: MediaViewModel.Companion.MediaType.MOVIES
                MediaScreen(mediaType = mediaType, navController = navController)
            }
        }
    }

    @Composable
    private fun TabBarView(
        text: String,
        selected: Boolean,
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
    ) {
        Tab(
            modifier = modifier,
            text = {
                Text(
                    fontWeight = if (selected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Medium
                    },
                    text = text,
                    color = Color.Black,
                )
            },
            selected = selected,
            onClick = onClick,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.updateActionBarTitle(getString(R.string.app_name))
        mainViewModel.updateActionBarNavIcon(null)
        (activity as AppCompatActivity).supportActionBar?.show()
        mainViewModel.updateBottomNav(true)
    }
}