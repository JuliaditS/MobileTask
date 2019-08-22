package com.codelabs.unikomradio.mvvm.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.unikomradio.utilities.base.BaseViewModel

class MainViewModel internal constructor() : BaseViewModel() {


    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    fun playStreaming() {
        _isPlaying.value = true
    }

    fun stopStreaming() {
        _isPlaying.value = false
    }

    fun stateStreaming(state: Boolean) {
        _isPlaying.value = state
    }

}