package com.codelabs.newunikomradio.mvvm.streaming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StreamingViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StreamingViewModel() as T
    }
}