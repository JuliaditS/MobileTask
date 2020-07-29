package com.codelabs.newunikomradio.utilities

import android.content.Context
import com.codelabs.newunikomradio.data.source.TopChartRepository
import com.codelabs.newunikomradio.data.source.local.AppDatabase
import com.codelabs.newunikomradio.mvvm.streaming.streaming_topcharts.StreamingTopchartsViewModelFactory

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

