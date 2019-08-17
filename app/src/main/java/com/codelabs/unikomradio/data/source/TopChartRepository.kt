package com.codelabs.unikomradio.data.source

import com.codelabs.unikomradio.data.model.TopChart
import com.codelabs.unikomradio.data.source.local.TopchartDao
import com.codelabs.unikomradio.data.source.remote.ApiService
import com.codelabs.unikomradio.utilities.TOPCHARTS
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class TopChartRepository private constructor(private val topchartDao: TopchartDao) {

    private val TAG by lazy { ProgramsRepository::class.java.simpleName }

    suspend fun getTopcharts(): List<TopChart> {
        val response = ApiService.topchartApiService.getTopcharts().await()
        try {
            if (response.isNotEmpty()) {
                topchartDao.insertAll(response)
            }
        } catch (e: Throwable) {
            Timber.d(e.localizedMessage.toString())
        }
        return response
    }


    fun getTopChart(topchartId: String) = topchartDao.getTopchart(topchartId)

    companion object {

        @Volatile
        private var instance: TopChartRepository? = null

        fun getInstance(topchartDao: TopchartDao) =
            instance ?: synchronized(this) {
                instance ?: TopChartRepository(topchartDao).also { instance = it }
            }
    }
}