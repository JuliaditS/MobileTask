package ac.id.unikom.codelabs.radio.data.source

import ac.id.unikom.codelabs.radio.data.model.TopChart
import ac.id.unikom.codelabs.radio.data.source.local.TopchartDao
import ac.id.unikom.codelabs.radio.data.source.remote.ApiService
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