package com.adifaisalr.tmdbapplication.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adifaisalr.tmdbapplication.libs.domain.model.getMediaType
import com.adifaisalr.tmdbapplication.presentation.ui.detail.MediaDetailScreen
import com.adifaisalr.tmdbapplication.presentation.ui.detail.MediaDetailViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.home.HomeScreen
import com.adifaisalr.tmdbapplication.presentation.ui.search.SearchScreen
import com.adifaisalr.tmdbapplication.presentation.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(navController = navController)
                }
                composable("search") {
                    val viewModel = hiltViewModel<SearchViewModel>()
                    SearchScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable("mediadetail/{type}/{id}") { backStackEntry ->
                    val viewModel = hiltViewModel<MediaDetailViewModel>()
                    val mediaType = backStackEntry.arguments?.getString("type")
                    val mediaId = backStackEntry.arguments?.getString("id")
                    if (mediaType == null || mediaId == null) return@composable
                    MediaDetailScreen(
                        navController = navController,
                        mediaType = mediaType.toInt().getMediaType(),
                        mediaId = mediaId.toInt(),
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}
