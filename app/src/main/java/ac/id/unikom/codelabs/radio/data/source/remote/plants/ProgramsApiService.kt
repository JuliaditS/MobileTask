package ac.id.unikom.codelabs.radio.data.source.remote.plants

import ac.id.unikom.codelabs.radio.data.model.Program
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ProgramsApiService {
    @GET("program")
    fun getPrograms(): Deferred<List<Program>>
}