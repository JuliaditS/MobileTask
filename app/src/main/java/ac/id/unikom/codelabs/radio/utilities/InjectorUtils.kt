package ac.id.unikom.codelabs.radio.utilities

import android.content.Context
import ac.id.unikom.codelabs.radio.data.source.TopChartRepository
import ac.id.unikom.codelabs.radio.data.source.local.AppDatabase
import ac.id.unikom.codelabs.radio.mvvm.streaming.streaming_topcharts.StreamingTopchartsViewModelFactory

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

