package com.adifaisalr.tmdbapplication.presentation.ui.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.search.LoadingItemView
import com.adifaisalr.tmdbapplication.presentation.ui.search.SearchItemView
import com.adifaisalr.tmdbapplication.presentation.util.NavigationUtils.safeNavigate

@Composable
fun FavoriteMediaScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: FavoriteMediaViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val listState = rememberLazyListState()
    val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()

    val onItemClick: ((SearchItem) -> Unit) = { item ->
        val mediaType = MediaViewModel.Companion.MediaType.values().find { it.type == item.mediaType }
        mediaType?.let {
            navController.safeNavigate("mediadetail/${mediaType.id}/${item.id}")
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        when {
            viewState.isLoading && viewState.favoriteMediaList.isEmpty() -> {
                item { LoadingItemView() }
            }

            viewState.favoriteMediaList.isEmpty() -> {
                item {
                    Text(text = stringResource(R.string.empty_favorite_media))
                }
            }

            else -> {
                items(viewState.favoriteMediaList.map { media ->
                    SearchItem(
                        id = media.id,
                        title = media.title,
                        posterPath = media.posterPath,
                        releaseDate = media.releaseDate,
                        rating = media.rating,
                        mediaType = media.type,
                    )
                }) { searchItem ->
                    SearchItemView(
                        searchItem = searchItem,
                        onItemClick = onItemClick,
                    )
                }
            }
        }
    }
}