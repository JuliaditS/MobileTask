package com.codelabs.unikomradio.mvvm.crew

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.unikomradio.data.model.Crew
import com.codelabs.unikomradio.utilities.CREW
import com.codelabs.unikomradio.utilities.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class CrewViewModel internal constructor() : BaseViewModel() {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection(CREW)
    var mediaPlayer: MediaPlayer? = null


    private val _crews = MutableLiveData<List<Crew>>()
    val crews: LiveData<List<Crew>>
        get() = _crews

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    init {
        start()
    }

    private fun start() {
        docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Timber.w("Listen failed.")
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                Timber.w("Current data: ${snapshot.documents}")
                val mutableList = mutableListOf<Crew>()
                for (document in snapshot.documents) {
                    document.toObject(Crew::class.java)?.let { mutableList.add(it) }
                }
                _crews.value = mutableList
                Timber.i("${crews.value}")

            } else {
                Timber.w("Current data null")
            }
        }
    }

    fun playStreaming() {
        _isPlaying.value = true
    }

    fun stopStreaming() {
        _isPlaying.value = false
    }

    fun stateStreaming(state:Boolean){
        _isPlaying.value = state
    }

}