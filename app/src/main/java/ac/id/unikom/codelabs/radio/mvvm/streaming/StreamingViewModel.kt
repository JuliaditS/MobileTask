package ac.id.unikom.codelabs.radio.mvvm.streaming

import ac.id.unikom.codelabs.radio.data.model.Crew
import ac.id.unikom.codelabs.radio.data.model.Program
import ac.id.unikom.codelabs.radio.utilities.ON_RADIO_PLAYING
import ac.id.unikom.codelabs.radio.utilities.PROGRAM
import ac.id.unikom.codelabs.radio.utilities.base.BaseViewModel
import ac.id.unikom.codelabs.radio.utilities.onradioplayingdocument
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import timber.log.Timber


class StreamingViewModel : BaseViewModel() {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection(PROGRAM)
    val docIsPlaying = db.collection(ON_RADIO_PLAYING).document(onradioplayingdocument)


    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _isMute = MutableLiveData<Boolean>()
    val isMute: LiveData<Boolean>
        get() = _isMute

    private val _programs = MutableLiveData<List<Program>>()
    val programs: LiveData<List<Program>>
        get() = _programs

    private val _onError = MutableLiveData<Boolean>()
    val onError: LiveData<Boolean>
        get() = _onError


    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    init {
        _isMute.value = false

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
            } else {
                Timber.w("Current data null")
            }
        }

        docIsPlaying.addSnapshotListener { snapshot, exception ->
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


    fun playStreaming() {
        _isPlaying.value = true
    }

    fun stopStreaming() {
        _isPlaying.value = false
    }

    fun muteStreaming() {
        _isMute.value = _isMute.value != true
    }

    fun isPlaying(): Boolean?{
        return _isPlaying.value
    }
    fun setStateStreaming(state: Boolean?) {
        _isPlaying.value = state
    }

}