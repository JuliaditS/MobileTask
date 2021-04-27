package ac.id.unikom.codelabs.radio.mvvm.news.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewsDetailViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsDetailViewModel() as T
    }

}