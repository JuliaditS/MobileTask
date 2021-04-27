package ac.id.unikom.codelabs.radio.mvvm.streaming.streaming_topcharts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ac.id.unikom.codelabs.radio.data.source.TopChartRepository

class StreamingTopchartsViewModelFactory(
    private val repository: TopChartRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StreamingTopchartsViewModel(repository) as T
    }
}