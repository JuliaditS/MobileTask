package com.codelabs.unikomradio.mvvm.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelabs.unikomradio.data.model.*
import com.codelabs.unikomradio.utilities.*
import com.codelabs.unikomradio.utilities.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class HomeViewModel internal constructor() : BaseViewModel() {
    val db = FirebaseFirestore.getInstance()
    private val docProgram = db.collection(PROGRAM)
    private val docBanner = db.collection(BANNER)
    private val docTopcharts = db.collection(TOPCHARTS)
    private val docCrew = db.collection(CREW)
    private val docNews = db.collection(NEWS)

    private val _programs = MutableLiveData<List<Program>>()
    val programs: LiveData<List<Program>>
        get() = _programs

    private val _banner = MutableLiveData<List<Banner>>()
    val banner: LiveData<List<Banner>>
        get() = _banner

    private val _topcharts = MutableLiveData<List<TopChart>>()
    val topcharts: LiveData<List<TopChart>>
        get() = _topcharts

    private val _crews = MutableLiveData<List<Crew>>()
    val crews: LiveData<List<Crew>>
        get() = _crews

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>>
        get() = _news


    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    init {
        initProgram()
        initBanner()
        initTopCharts()
        initCrews()
        initNews()
    }

    private fun initBanner() {
        docBanner.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Timber.w("Listen failed.")
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val mutableList = mutableListOf<Banner>()

                for (document in snapshot.documents) {
                    document.toObject(Banner::class.java)?.let { mutableList.add(it) }

                }
                _banner.value = mutableList

            } else {
                Timber.w("Current data null")
            }
        }
    }

    private fun initProgram() {
        docProgram.addSnapshotListener { snapshot, exception ->
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
    }

    fun initTopCharts() {
        docTopcharts.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Timber.w("Listen failed.")
                exception.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val mutableList = mutableListOf<TopChart>()
                for (document in snapshot.documents) {
                    document.toObject(TopChart::class.java)?.let { mutableList.add(it) }
                }
                _topcharts.value = mutableList
            } else {
                Timber.w("Current data null")
            }
        }
    }

    private fun initCrews() {
        docCrew.addSnapshotListener { snapshot, exception ->
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

    private fun initNews(){
        docNews.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Timber.w("Listen failed.")
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