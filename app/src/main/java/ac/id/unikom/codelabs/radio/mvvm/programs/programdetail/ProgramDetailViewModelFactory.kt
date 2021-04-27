package ac.id.unikom.codelabs.radio.mvvm.programs.programdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProgramDetailViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProgramDetailViewModel() as T
    }

}