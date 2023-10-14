@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package com.adifaisalr.tmdbapplication.presentation.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import com.adifaisalr.tmdbapplication.presentation.util.NavigationUtils.safeNavigate
import com.adifaisalr.tmdbapplication.presentation.util.OnBottomReached

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val keyboard = LocalSoftwareKeyboardController.current
    var keyword by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()

    val onItemClick: ((SearchItem) -> Unit) = { item ->
        val mediaType = MediaViewModel.Companion.MediaType.values().find { it.type == item.mediaType }
        mediaType?.let {
            navController.safeNavigate("mediadetail/${mediaType.id}/${item.id}")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.search),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = keyword,
                    onValueChange = {
                        viewModel.setSearched(false)
                        keyword = it
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboard?.hide()
                            viewModel.setQuery(keyword)
                            viewModel.loadNextPage()
                        }
                    )
                )
                Button(
                    onClick = {
                        keyboard?.hide()
                        viewModel.setQuery(keyword)
                        viewModel.loadNextPage()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "search icon"
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                when {
                    viewState.isLoading && viewState.searchItemList.isEmpty() -> {
                        item { LoadingItemView() }
                    }

                    viewState.searchItemList.isEmpty() && viewState.isSearched && keyword.isNotEmpty() -> {
                        item {
                            Text(text = stringResource(R.string.empty_search_result))
                        }
                    }

                    else -> {
                        items(viewState.searchItemList.filter { it.mediaType != "person" }) { searchItem ->
                            SearchItemView(
                                searchItem = searchItem,
                                onItemClick = onItemClick,
                            )
                        }
                        if (!viewState.isLastBatch && viewState.isLoading) {
                            item { LoadingItemView() }
                        }
                    }
                }
            }
            listState.OnBottomReached(isLastPage = viewState.isLastBatch) {
                viewModel.loadNextPage()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoadingItemView() {
    LoadingItemView()
}

@Composable
fun SearchItemView(
    searchItem: SearchItem,
    onItemClick: (SearchItem) -> Unit,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemClick(searchItem) }) {
        AsyncImage(
            modifier = Modifier.width(50.dp),
            model = Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W92 + searchItem.posterPath,
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            error = ColorPainter(Color.LightGray),
            fallback = ColorPainter(Color.LightGray),
            contentDescription = "",
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = searchItem.title,
        )
    }
}

@Composable
fun LoadingItemView(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
        )
    }
}