package com.codelabs.unikomradio.mvvm.streaming

import androidx.lifecycle.ViewModelProvider
import com.codelabs.unikomradio.data.source.ProgramsRepository

class StreamingViewModelFactory(
        private val programsRepository: ProgramsRepository
) : ViewModelProvider.NewInstanceFactory()