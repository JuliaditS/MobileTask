package com.codelabs.unikomradio.mvvm.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.mvvm.programs.specifyToday
import com.codelabs.unikomradio.utilities.ISPLAYING
import com.codelabs.unikomradio.utilities.ON_RADIO_PLAYING
import com.codelabs.unikomradio.utilities.PROGRAM
import com.codelabs.unikomradio.utilities.base.BaseViewModel
import com.codelabs.unikomradio.utilities.onradioplayingdocument
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class MainViewModel internal constructor() : BaseViewModel() {


    val db = FirebaseFirestore.getInstance()
    private val docRef = db.collection(ON_RADIO_PLAYING).document(onradioplayingdocument)
    private val docProgram = db.collection(PROGRAM)


    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _onError = MutableLiveData<Boolean>()
    val onError: LiveData<Boolean>
        get() = _onError

    private val _todayProgram = MutableLiveData<Program>()
    val todayProgram: LiveData<Program>
        get() = _todayProgram

    init {
        _isPlaying.value = false
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

        docProgram.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Timber.w("Listen failed.")
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                for (document in snapshot.documents) {
                    try {
                        val program = document.toObject(Program::class.java)

                        if (program != null) {
                            if (specifyToday.isToday(program.heldDay)) {
                                _todayProgram.value = program
                            } else {
                                val dummyDay: String = program.heldDay.replace(" ", "")
                                val dummyDayString = dummyDay.split("-")
                                if (specifyToday.isToday(dummyDayString[0], dummyDayString[1])) {
                                    if ((program.startAt.toDouble() <= specifyToday.getHourSpecify()) && (program.endAt.toDouble() >= specifyToday.getHourSpecify())) {
                                        _todayProgram.value = program
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Timber.i(e.localizedMessage)
                    }
                }
            } else {
                Timber.w("Current data null")
            }
        }
    }


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
}
