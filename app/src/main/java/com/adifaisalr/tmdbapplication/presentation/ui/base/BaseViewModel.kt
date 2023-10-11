package com.adifaisalr.tmdbapplication.presentation.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

abstract class BaseViewModel<VS : BaseViewModel.ViewState, AR : BaseViewModel.ActionResult>(
    initialState: VS,
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(initialState)
    val stateFlow = _stateFlow.asStateFlow()

    protected abstract fun reducer(oldState: VS, actionResult: AR): VS

    fun handleActionResult(newActionResult: AR) {
        Timber.i("Action Result: $newActionResult")
        val newState = reducer(_stateFlow.value, newActionResult)
        Timber.i("New state callEffect: $newState")
        _stateFlow.value = newState
    }

    // UI State
    interface ViewState

    // Result of action that will pass to reducer
    interface ActionResult
}