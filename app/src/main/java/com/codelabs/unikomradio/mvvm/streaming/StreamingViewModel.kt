package com.codelabs.unikomradio.mvvm.streaming

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codelabs.unikomradio.utilities.base.BaseViewModel
import com.codelabs.unikomradio.utilities.services.MediaPlayerServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel


class StreamingViewModel : BaseViewModel() {

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _isMute = MutableLiveData<Boolean>()
    val isMute: LiveData<Boolean>
        get() = _isMute


    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    init {
        _isMute.value = false
    }


    fun playStreaming() {
        _isPlaying.value = true
    }

    fun stopStreaming() {
        _isPlaying.value = false
    }

    fun muteStreaming() {
      _isMute.value = _isMute.value != true
    }

    fun stateStreaming(state:Boolean){
        _isPlaying.value = state
    }
}