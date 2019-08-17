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

    val url = "http://hits.unikom.ac.id:9996/;listen.pls?sid=1"
    var mediaPlayer: MediaPlayer? = null

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
        _isPlaying.value = false
        _isMute.value = false
    }


    fun playStreaming() {
        _isPlaying.value = true
    }

    fun stopStreaming() {
        _isPlaying.value = false
//        context?.stopService(intent)
    }

    fun muteStreaming() {
      _isMute.value = _isMute.value != true
    }
}