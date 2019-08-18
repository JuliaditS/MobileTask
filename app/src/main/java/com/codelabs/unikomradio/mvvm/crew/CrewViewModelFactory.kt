package com.codelabs.unikomradio.mvvm.crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CrewViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrewViewModel() as T
    }
}