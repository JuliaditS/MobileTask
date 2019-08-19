package com.codelabs.unikomradio.mvvm.programs.programdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProgramDetailViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProgramDetailViewModel() as T
    }

}