package com.codelabs.newunikomradio.mvvm.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.newunikomradio.data.model.Crew
import com.codelabs.newunikomradio.data.model.Program
import com.codelabs.newunikomradio.utilities.PROGRAM
import com.codelabs.newunikomradio.utilities.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class ProgramViewModel internal constructor() : BaseViewModel() {
    var firstLoad = true
    var noresultsearchtext = ""

    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection(PROGRAM)

    private val _programs = MutableLiveData<List<Program>>()
    val programs: LiveData<List<Program>>
        get() = _programs

    private val _resultprograms = MutableLiveData<List<Program>>()
    val resultprograms: LiveData<List<Program>>
        get() = _resultprograms

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying


    init {
        start()
    }

    fun start() {

        docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Timber.w("Listen failed.")
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val mutableList = mutableListOf<Program>()
                for ((i, document) in snapshot.documents.withIndex()) {
                    document.toObject(Program::class.java)?.let { mutableList.add(it) }
                    try {
                        val crews = document["crew"] as List<Map<String, Any>>?
                        if (crews != null) {
                            for (crew in crews) {
                                val crewObject = Crew(
                                    -1,
                                    crew.get("userPhoto") as String? ?: "",
                                    crew.get("name") as String? ?: "",
                                    crew.get("role") as String? ?: ""
                                )
                                mutableList[i].announcer.add(crewObject)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Timber.d("crewsMap : ${e.message}")
                    }
                }

                _programs.value = mutableList
                firstLoad = false
            } else {
                Timber.w("Current data null")
            }
        }
    }


    fun searchPrograms(searchText: String) {
        noresultsearchtext = searchText
        val resultPrograms = mutableListOf<Program>()
        if (programs.value?.isNotEmpty()!!){
            for (i in 0 until programs.value?.size!!){
                if (programs.value!![i].title.toUpperCase().contains(searchText.toUpperCase())){
                    resultPrograms.add(programs.value!![i])
                }
            }
        }
        _resultprograms.value = resultPrograms
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