package com.codelabs.unikomradio.mvvm.streaming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codelabs.unikomradio.data.source.ProgramsRepository
import com.codelabs.unikomradio.utilities.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

class StreamingViewModel(
        programsRepository: ProgramsRepository
) : BaseViewModel() {
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

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
    }

}