package com.adifaisalr.tmdbapplication.presentation.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.presentation.ui.home.HomeFragmentDirections
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.search.LoadingItemView
import com.adifaisalr.tmdbapplication.presentation.ui.search.SearchItemView
import com.adifaisalr.tmdbapplication.presentation.util.NavigationUtils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMediaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FavoriteMediaScreen()
            }
        }
    }

    @Composable
    private fun FavoriteMediaScreen(
        modifier: Modifier = Modifier,
        viewModel: FavoriteMediaViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    ) {
        val listState = rememberLazyListState()
        val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()

        val onItemClick: ((SearchItem) -> Unit) = { item ->
            val mediaType = MediaViewModel.Companion.MediaType.values().find { it.type == item.mediaType }
            mediaType?.let {
                val action = HomeFragmentDirections.actionGlobalMediaDetailFragment(item.id, it)
                findNavController().safeNavigate(action)
            }
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.loadData()
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            when {
                viewState.isLoading && viewState.favoriteMediaList.isEmpty() -> {
                    item { LoadingItemView() }
                }

                viewState.favoriteMediaList.isEmpty() -> {
                    item {
                        Text(text = getString(R.string.empty_favorite_media))
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
}