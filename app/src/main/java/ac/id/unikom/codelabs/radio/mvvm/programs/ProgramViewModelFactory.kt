package ac.id.unikom.codelabs.radio.mvvm.programs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProgramViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProgramViewModel() as T
    }
}