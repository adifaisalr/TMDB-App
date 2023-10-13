package com.adifaisalr.tmdbapplication.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.data.api.Api
import com.adifaisalr.tmdbapplication.domain.model.Review
import com.adifaisalr.tmdbapplication.presentation.ui.MainViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.components.ShimmerBrush
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaDetailFragment : Fragment() {

    private val viewModel by viewModels<MediaDetailViewModel>()
    val mainViewModel: MainViewModel by activityViewModels()

    val args: MediaDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mediaId = args.mediaId
        viewModel.mediaType = args.mediaType
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MediaDetailScreen(viewModel)
            }
        }
    }

    @Composable
    fun MediaDetailScreen(
        viewModel: MediaDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    ) {
        val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()
        MediaDetailContent(viewState = viewState)
    }

    @Composable
    fun MediaDetailContent(
        modifier: Modifier = Modifier,
        viewState: MediaDetailViewState,
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
                    val media = viewState.media!!
                    mainViewModel.updateActionBarTitle(media.title)
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
                                onClick = { viewModel.changeFavoriteMedia() }) {
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
            viewState = MediaDetailViewState()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        mainViewModel.updateBottomNav(false)
        viewModel.getMovieDetail()
    }
}