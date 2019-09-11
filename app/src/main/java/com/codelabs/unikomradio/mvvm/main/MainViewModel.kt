package com.codelabs.unikomradio.mvvm.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.unikomradio.utilities.ISPLAYING
import com.codelabs.unikomradio.utilities.ON_RADIO_PLAYING
import com.codelabs.unikomradio.utilities.base.BaseViewModel
import com.codelabs.unikomradio.utilities.onradioplayingdocument
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class MainViewModel internal constructor() : BaseViewModel() {


    val db = FirebaseFirestore.getInstance()
    private val docRef = db.collection(ON_RADIO_PLAYING).document(onradioplayingdocument)

    private val _onError = MutableLiveData<Boolean>()
    val onError: LiveData<Boolean>
        get() = _onError

    init {
        _onError.value = false
        docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                exception.printStackTrace()
                _onError.value = true
                return@addSnapshotListener
            }

            if (snapshot != null) {
                _isPlaying.value = snapshot.data?.get("isPlaying").toString().toBoolean()
            } else {
                exception?.printStackTrace()
                Timber.w("Current data null")
            }
        }
    }

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    fun playStreaming() {
        docRef.set(hashMapOf(ISPLAYING to true))
        _isPlaying.value = true
    }

    fun stopStreaming() {
        docRef.set(hashMapOf(ISPLAYING to false))
        _isPlaying.value = false
    }

    fun stateStreaming(state: Boolean) {
        _isPlaying.value = state
    }

    init {
        _isPlaying.value = false
    }
}
