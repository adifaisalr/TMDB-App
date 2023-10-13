package com.adifaisalr.tmdbapplication.presentation.ui.favorite

import androidx.lifecycle.viewModelScope
import com.adifaisalr.tmdbapplication.domain.model.Media
import com.adifaisalr.tmdbapplication.domain.usecase.LoadFavoriteMediaUseCase
import com.adifaisalr.tmdbapplication.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMediaViewModel @Inject constructor(
    private val loadFavoriteMediaUseCase: LoadFavoriteMediaUseCase
) : BaseViewModel<FavoriteMediaViewState, FavoriteMediaActionResult>(
    initialState = FavoriteMediaViewState()
) {
    protected var favoriteMediaList: List<Media> = emptyList()

    fun loadData() {
        loadFavoriteMedia()
    }

    private fun loadFavoriteMedia() = viewModelScope.launch {
        handleActionResult(FavoriteMediaActionResult.SetShowLoading(true))
        val favoriteMediaList = loadFavoriteMediaUseCase.loadAll()
        handleActionResult(FavoriteMediaActionResult.SetFavoriteMediaList(favoriteMediaList))
    }

    override fun reducer(
        oldState: FavoriteMediaViewState,
        actionResult: FavoriteMediaActionResult
    ): FavoriteMediaViewState {
        return FavoriteMediaViewState(
            favoriteMediaList = favoriteMediaListReducer(actionResult),
            isLoading = isLoadingReducer(actionResult),
        )
    }

    private fun favoriteMediaListReducer(actionResult: FavoriteMediaActionResult): List<Media> {
        favoriteMediaList = when (actionResult) {
            is FavoriteMediaActionResult.SetShowLoading -> emptyList()
            is FavoriteMediaActionResult.SetFavoriteMediaList -> actionResult.favoriteMediaList
        }
        return favoriteMediaList
    }

    private fun isLoadingReducer(actionResult: FavoriteMediaActionResult): Boolean {
        return when (actionResult) {
            is FavoriteMediaActionResult.SetShowLoading -> actionResult.isShown
            else -> false
        }
    }
}