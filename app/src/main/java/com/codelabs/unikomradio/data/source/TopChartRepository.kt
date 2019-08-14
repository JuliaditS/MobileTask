package com.codelabs.unikomradio.data.source

import android.util.Log
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.data.model.TopChart
import com.codelabs.unikomradio.data.source.local.ProgramDao
import com.codelabs.unikomradio.data.source.local.TopchartDao
import com.codelabs.unikomradio.data.source.remote.ApiService

class TopChartRepository private constructor(private val topchartDao: TopchartDao) {

    private val TAG by lazy { ProgramsRepository::class.java.simpleName }

    suspend fun getTopcharts(): List<TopChart> {
        val response = ApiService.topchartApiService.getTopcharts().await()
        try {
            if (response.isNotEmpty()) {
                topchartDao.insertAll(response)
            }
        } catch (e: Throwable) {
            Log.e(TAG, e.toString())
        }
        return response
    }

    fun getTopChart(topchartId:String) = topchartDao.getTopchart(topchartId)

    companion object {

        @Volatile private var instance: TopChartRepository? = null

        fun getInstance(topchartDao: TopchartDao) =
                instance ?: synchronized(this){
                    instance ?: TopChartRepository(topchartDao).also { instance = it }
                }
    }
}