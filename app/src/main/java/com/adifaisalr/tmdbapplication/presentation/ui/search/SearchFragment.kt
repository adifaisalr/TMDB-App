package com.adifaisalr.tmdbapplication.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.presentation.ui.MainViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.home.HomeFragmentDirections
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import com.adifaisalr.tmdbapplication.presentation.util.NavigationUtils.safeNavigate
import com.adifaisalr.tmdbapplication.presentation.util.OnBottomReached
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel>()
    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SearchScreen(viewModel)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun SearchScreen(
        viewModel: SearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    ) {
        val keyboard = LocalSoftwareKeyboardController.current
        var keyword by remember { mutableStateOf("") }
        val listState = rememberLazyListState()
        val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()

        val onItemClick: ((SearchItem) -> Unit) = { item ->
            val mediaType = MediaViewModel.Companion.MediaType.values().find { it.type == item.mediaType }
            mediaType?.let {
                val action = HomeFragmentDirections.actionGlobalMediaDetailFragment(item.id, it)
                findNavController().safeNavigate(action)
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = keyword,
                    onValueChange = {
                        keyword = it
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboard?.hide()
                            doSearch(keyword)
                        }
                    )
                )
                Button(
                    onClick = {
                        keyboard?.hide()
                        doSearch(keyword)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "search icon"
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                when {
                    viewState.isLoading && viewState.searchItemList.isEmpty() -> {
                        item { LoadingItemView() }
                    }

                    viewState.searchItemList.isEmpty() && keyword.isNotEmpty() -> {
                        item {
                            Text(text = getString(R.string.empty_search_result))
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

    @Preview(showBackground = true)
    @Composable
    private fun PreviewLoadingItemView() {
        LoadingItemView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        mainViewModel.updateBottomNav(false)
    }

    private fun doSearch(keyword: String) {
        viewModel.setQuery(keyword)
        viewModel.loadNextPage()
        Toast.makeText(requireContext(), keyword, Toast.LENGTH_SHORT).show()
    }
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