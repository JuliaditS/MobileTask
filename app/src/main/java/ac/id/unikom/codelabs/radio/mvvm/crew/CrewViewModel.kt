package ac.id.unikom.codelabs.radio.mvvm.crew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ac.id.unikom.codelabs.radio.data.model.Crew
import ac.id.unikom.codelabs.radio.utilities.CREW
import ac.id.unikom.codelabs.radio.utilities.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class CrewViewModel internal constructor() : BaseViewModel() {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection(CREW)


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
                val mutableList = mutableListOf<Crew>()
                for (document in snapshot.documents) {
                    document.toObject(Crew::class.java)?.let { mutableList.add(it) }
                }
                _crews.value = mutableList
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