@file:OptIn(ExperimentalFoundationApi::class)

package com.adifaisalr.tmdbapplication.presentation.ui.media

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.libs.domain.model.Media
import com.adifaisalr.tmdbapplication.libs.domain.model.MediaType
import com.adifaisalr.tmdbapplication.presentation.ui.components.ShimmerBrush
import com.adifaisalr.tmdbapplication.presentation.util.NavigationUtils.safeNavigate
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@Composable
fun MediaScreen(
    modifier: Modifier = Modifier,
    mediaType: MediaType,
    navController: NavController,
    viewModel: MediaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()
    val actionClickListener: ((Media) -> Unit) = { media ->
        navController.safeNavigate("mediadetail/${mediaType.id}/${media.id}")
    }
    viewModel.mediaType = mediaType

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchAllData()
    }

    Column(modifier = modifier.fillMaxSize()) {
        PopularMedia(
            mediaViewState = viewState.popularMediaViewState,
            onItemClick = actionClickListener,
        )
        Spacer(modifier = Modifier.height(5.dp))
        CarouselMediaSection(
            title = "Trending",
            mediaViewState = viewState.trendingMediaViewState,
            onItemClick = actionClickListener,
        )
        Spacer(modifier = Modifier.height(5.dp))
        CarouselMediaSection(
            title = "Discover",
            mediaViewState = viewState.discoverMediaViewState,
            onItemClick = actionClickListener,
        )
    }
}

@Composable
private fun PopularMedia(
    modifier: Modifier = Modifier,
    mediaViewState: MediaSectionViewState,
    onItemClick: (Media) -> Unit,
) {
    val pagerState = rememberPagerState {
        mediaViewState.media?.results?.size ?: 0
    }
    LaunchedEffect(pagerState.pageCount) {
        while (pagerState.pageCount > 1) {
            yield()
            delay(2000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount),
                animationSpec = tween(600)
            )
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when {
            mediaViewState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            !mediaViewState.errorMessage.isNullOrEmpty() -> {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = mediaViewState.errorMessage,
                )
            }

            else -> {
                HorizontalPager(
                    modifier = Modifier,
                    state = pagerState
                ) { page ->
                    val media = mediaViewState.media?.results?.get(page)
                    media?.let {
                        Box(
                            modifier = Modifier.clickable {
                                onItemClick(media)
                            },
                        ) {
                            AsyncImage(
                                model = Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W1280 + media.backdropPath,
                                fallback = painterResource(id = R.drawable.ic_launcher_background),
                                error = painterResource(id = R.drawable.ic_launcher_background),
                                contentScale = ContentScale.FillWidth,
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.BottomStart)
                                    .shadow(
                                        elevation = 15.dp,
                                        spotColor = Color.White, ambientColor = Color.White
                                    ),
                                text = media.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarouselMediaSection(
    modifier: Modifier = Modifier,
    title: String,
    mediaViewState: MediaSectionViewState,
    onItemClick: (Media) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        LazyRow(
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            when {
                mediaViewState.isLoading -> {
                    items(3) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(ShimmerBrush()),
                        )
                    }
                }

                !mediaViewState.errorMessage.isNullOrEmpty() -> {
                    item {
                        Text(
                            modifier = Modifier.height(100.dp),
                            text = mediaViewState.errorMessage,
                        )
                    }
                }

                else -> {
                    val list = mediaViewState.media?.results ?: emptyList()
                    items(list) { item ->
                        AsyncImage(
                            modifier = Modifier
                                .width(100.dp)
                                .clickable {
                                    onItemClick(item)
                                },
                            model = Api.DEFAULT_BASE_IMAGE_URL + Api.IMAGE_SIZE_W92 + item.posterPath,
                            contentDescription = item.title,
                            contentScale = ContentScale.FillWidth,
                            placeholder = ColorPainter(Color.LightGray),
                            error = ColorPainter(Color.LightGray),
                            fallback = ColorPainter(Color.LightGray),
                        )
                    }
                }
            }
        }
    }
}