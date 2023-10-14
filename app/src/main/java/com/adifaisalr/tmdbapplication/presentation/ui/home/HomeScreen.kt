@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.adifaisalr.tmdbapplication.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.libs.domain.model.MediaType
import com.adifaisalr.tmdbapplication.presentation.ui.favorite.FavoriteMediaScreen
import com.adifaisalr.tmdbapplication.presentation.ui.favorite.FavoriteMediaViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaScreen
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState { 3 }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },

        bottomBar = {
            val items = listOf(
                BottomNavItem.Movie,
                BottomNavItem.TV,
                BottomNavItem.Profile
            )

            NavigationBar {
                items.forEachIndexed { index, item ->
                    AddItem(
                        screen = item,
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index, 0f)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            modifier = Modifier.padding(innerPadding),
            state = pagerState,
            userScrollEnabled = false,
        ) { page ->
            when (page) {
                0 -> {
                    val viewModel = hiltViewModel<MediaViewModel>()
                    MediaScreen(
                        mediaType = MediaType.MOVIES,
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                1 -> {
                    val viewModel = hiltViewModel<MediaViewModel>()
                    MediaScreen(
                        mediaType = MediaType.TV_SHOWS,
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                else -> {
                    val viewModel = hiltViewModel<FavoriteMediaViewModel>()
                    FavoriteMediaScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        // Text that shows bellow the icon
        label = {
            Text(text = screen.title)
        },

        // The icon resource
        icon = {
            Icon(
                painterResource(id = screen.icon),
                contentDescription = screen.title
            )
        },

        // Display if the icon it is select or not
        selected = selected,

        // Always show the label bellow the icon or not
        alwaysShowLabel = true,

        // Click listener for the icon
        onClick = onClick,

        // Control all the colors of the icon
        colors = NavigationBarItemDefaults.colors()
    )
}

sealed class BottomNavItem(
    var title: String,
    var icon: Int,
) {
    data object Movie :
        BottomNavItem(
            "Movie",
            R.drawable.baseline_movie_24,
        )

    data object TV :
        BottomNavItem(
            "TV",
            R.drawable.baseline_live_tv_24,
        )

    data object Profile :
        BottomNavItem(
            "Favorite",
            R.drawable.baseline_star_rate_24,
        )
}