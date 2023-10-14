@file:OptIn(ExperimentalMaterial3Api::class)

package com.adifaisalr.tmdbapplication.presentation.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.domain.model.Review
import com.adifaisalr.tmdbapplication.presentation.ui.components.ShimmerBrush
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel

@Composable
fun MediaDetailScreen(
    mediaType: MediaViewModel.Companion.MediaType,
    mediaId: Int,
    navController: NavController,
    viewModel: MediaDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    viewModel.mediaId = mediaId
    viewModel.mediaType = mediaType
    val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.getMovieDetail()
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
                        viewState.media?.title ?: "",
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
                actions = {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        MediaDetailContent(
            modifier = Modifier.padding(innerPadding),
            viewState = viewState,
            onClickFavorite = { viewModel.changeFavoriteMedia() },
        )
    }
}

@Composable
fun MediaDetailContent(
    modifier: Modifier = Modifier,
    viewState: MediaDetailViewState,
    onClickFavorite: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            viewState.isMediaLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            !viewState.error.isNullOrEmpty() -> {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = viewState.error,
                )
            }

            else -> {
                viewState.media?.let { media ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .height(200.dp),
                            model = Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W1280 + media.backdropPath,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .weight(1f)
                                    .fillMaxWidth(),
                                text = String.format("%.1f", media.rating),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            val text = if (viewState.isFavorite) "Remove from favorite"
                            else "Add to favorite"
                            Button(
                                modifier = Modifier
                                    .wrapContentSize(),
                                enabled = !viewState.isFavoriteLoading,
                                onClick = onClickFavorite,
                            ) {
                                Text(text = text)
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_star_outline_24),
                                    contentDescription = null,
                                )
                            }
                        }
                        Divider()
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            text = "Overview",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = media.overview,
                            fontSize = 14.sp,
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 5.dp),
                            text = "Release Date",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = media.releaseDate,
                            fontSize = 14.sp,
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 5.dp),
                            text = "Review",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        when {
                            viewState.isReviewLoading -> {
                                ItemReviewShimmer(Modifier.padding(vertical = 5.dp))
                            }

                            viewState.reviewItemList.isEmpty() -> {
                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = "No Review"
                                )
                            }

                            else -> {
                                viewState.reviewItemList.forEach {
                                    ItemReview(review = it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemReview(
    review: Review,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                model = Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W92 + review.authorDetails.avatarPath,
                contentDescription = "",
                error = painterResource(id = R.drawable.ic_launcher_background),
                fallback = painterResource(id = R.drawable.ic_launcher_background),
            )
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = review.author
            )
        }
        Text(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            text = review.content,
            fontSize = 12.sp,
            maxLines = 3,
        )
    }
}

@Composable
fun ItemReviewShimmer(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ShimmerBrush()),
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .width(100.dp)
                    .height(20.dp)
                    .background(ShimmerBrush()),
            )
        }
        Box(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .height(20.dp)
                .background(ShimmerBrush()),
        )
    }
}

@Preview
@Composable
fun ReviewItemPreview() {
    ItemReview(
        review = Review(
            author = "Author",
            authorDetails = Review.AuthorDetails("", "Author", 5.0, "author"),
            content = "Review content",
            createdAt = "",
            updatedAt = "",
            id = "",
            url = "",
        )
    )
}

@Preview
@Composable
fun ItemReviewShimmerPreview() {
    ItemReviewShimmer()
}

@Preview
@Composable
fun MediaDetailScreenPreview() {
    MediaDetailContent(
        viewState = MediaDetailViewState(),
        onClickFavorite = {},
    )
}