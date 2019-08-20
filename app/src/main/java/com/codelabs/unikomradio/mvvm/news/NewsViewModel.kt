package com.codelabs.unikomradio.mvvm.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.unikomradio.data.model.News
import com.codelabs.unikomradio.utilities.NEWS
import com.codelabs.unikomradio.utilities.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class NewsViewModel : BaseViewModel() {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection(NEWS)

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>>
        get() = _news

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
                Timber.w("Current news data: ${snapshot.documents}")
                val mutableList = mutableListOf<News>()

                for (document in snapshot.documents) {
                    document.toObject(News::class.java)?.let { mutableList.add(it) }

                }
                _news.value = mutableList
                Timber.i("news programs ${news.value}")

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