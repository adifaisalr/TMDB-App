package com.adifaisalr.tmdbapplication.presentation.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow

@Composable
fun LazyListState.OnBottomReached(
    isLastPage: Boolean,
    loadMore: () -> Unit,
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore, isLastPage) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it && !isLastPage) {
                    loadMore()
                }
            }
    }
}
