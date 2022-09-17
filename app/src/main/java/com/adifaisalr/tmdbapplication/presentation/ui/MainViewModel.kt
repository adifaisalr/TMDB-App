package com.adifaisalr.tmdbapplication.presentation.ui

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _navigationIcon = MutableLiveData<Drawable?>()
    val navigationIcon: LiveData<Drawable?>
        get() = _navigationIcon

    fun updateActionBarTitle(title: String) = _title.postValue(title)
    fun updateActionBarNavIcon(icon: Drawable?) = _navigationIcon.postValue(icon)
}