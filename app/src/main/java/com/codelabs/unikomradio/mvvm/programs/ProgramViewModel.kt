package com.codelabs.unikomradio.mvvm.programs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.unikomradio.data.model.Crew
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.utilities.PROGRAM
import com.codelabs.unikomradio.utilities.base.BaseViewModel
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
                    val crewMap: HashMap<String, Any?>? = document["crew"] as HashMap<String, Any?>?
                    val crew = Crew(
                        -1,
                        crewMap?.get("userPhoto") as String? ?: "",
                        crewMap?.get("name") as String? ?: "",
                        crewMap?.get("role") as String? ?: ""
                    )
                    mutableList[i].announcer.add(crew)
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