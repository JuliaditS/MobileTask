package com.codelabs.unikomradio.utilities

import android.content.Context
import com.codelabs.unikomradio.data.source.TopChartRepository
import com.codelabs.unikomradio.data.source.local.AppDatabase
import com.codelabs.unikomradio.mvvm.streaming.streaming_topcharts.StreamingTopchartsViewModelFactory

object InjectorUtils {
    private fun getTopCartRepository(context: Context): TopChartRepository{
        return TopChartRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).topchartDao()
        )
    }

    fun provideTopchartViewModelFactory(context: Context): StreamingTopchartsViewModelFactory{
        val repository = getTopCartRepository(context)
        return StreamingTopchartsViewModelFactory(repository)
    }

}

