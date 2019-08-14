package com.codelabs.unikomradio.utilities.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codelabs.unikomradio.utilities.helper.Event

open class BaseViewModel : ViewModel() {

    val isRequesting = MutableLiveData<Event<Boolean>>()
    val showMessage = MutableLiveData<Event<String>>()
    val showMessageRes = MutableLiveData<Event<Int>>()
}