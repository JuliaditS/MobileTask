package ac.id.unikom.codelabs.radio.mvvm.streaming.streaming_topcharts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ac.id.unikom.codelabs.radio.data.model.TopChart
import ac.id.unikom.codelabs.radio.data.source.TopChartRepository
import ac.id.unikom.codelabs.radio.utilities.TOPCHARTS
import ac.id.unikom.codelabs.radio.utilities.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class StreamingTopchartsViewModel internal constructor(private val repository: TopChartRepository) : BaseViewModel() {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection(TOPCHARTS)

    private val _topcharts = MutableLiveData<List<TopChart>>()
    val topcharts: LiveData<List<TopChart>>
        get() = _topcharts

    fun getTopcharts() {
        docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Timber.w("Listen failed.")
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                Timber.w("Current data: ${snapshot.documents}")
                val mutableList = mutableListOf<TopChart>()
                for (document in snapshot.documents) {
                    document.toObject(TopChart::class.java)?.let { mutableList.add(it) }
                }
                _topcharts.value = mutableList
                Timber.i("${topcharts.value}")

            } else {
                Timber.w("Current data null")
            }
        }
    }
}