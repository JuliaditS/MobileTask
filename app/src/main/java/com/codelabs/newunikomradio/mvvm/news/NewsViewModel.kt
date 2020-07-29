package com.codelabs.newunikomradio.mvvm.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.newunikomradio.data.model.News
import com.codelabs.newunikomradio.utilities.NEWS
import com.codelabs.newunikomradio.utilities.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class NewsViewModel : BaseViewModel() {

    var noresultsearchtext = ""


    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection(NEWS)

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>>
        get() = _news

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _resultNews = MutableLiveData<List<News>>()
    val resultNews: LiveData<List<News>>
        get() = _resultNews

    init {
        start()
    }

    fun start() {
        docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val mutableList = mutableListOf<News>()

                for (document in snapshot.documents) {
                    document.toObject(News::class.java)?.let { mutableList.add(it) }

                }
                _news.value = mutableList

            } else {
                exception?.printStackTrace()
                Timber.w("Current data null")
            }
        }
    }

    fun searchNews(searchText: String) {
        noresultsearchtext = searchText
        val resultNews = mutableListOf<News>()
        if (news.value?.isNotEmpty()!!){
            for (i in 0 until news.value?.size!!){
                if (news.value!![i].title.toUpperCase().contains(searchText.toUpperCase())){
                    resultNews.add(news.value!![i])
                }
            }
        }
        _resultNews.value = resultNews
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