package com.codelabs.unikomradio.mvvm.programs

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.unikomradio.data.model.Crew
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.utilities.PROGRAM
import com.codelabs.unikomradio.utilities.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class ProgramViewModel internal constructor() : BaseViewModel() {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection(PROGRAM)

    private val _programs = MutableLiveData<List<Program>>()
    val programs: LiveData<List<Program>>
        get() = _programs

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
                val mutableList = mutableListOf<Program>()
                val announcerList = mutableListOf<Crew>()

                val announcer: HashMap<String, String> = HashMap<String, String>()

                var i = 0
                for (document in snapshot.documents) {
                    document.toObject(Program::class.java)?.let { mutableList.add(it) }
                    val crewMap: HashMap<String, Any?>? = document["crew"] as HashMap<String, Any?>?
                    val crew = Crew(
                        -1,
                        crewMap?.get("userPhoto") as String? ?: "",
                        crewMap?.get("name") as String? ?: "",
                        crewMap?.get("role") as String? ?: ""
                    )
                    mutableList[i].announcer.add(crew)
                    i++
                }
                _programs.value = mutableList
                Timber.i("data programs ${programs.value}")

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

    fun stateStreaming(state: Boolean) {
        _isPlaying.value = state
    }

}