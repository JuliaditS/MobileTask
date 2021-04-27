package ac.id.unikom.codelabs.radio.data.source.remote.plants

import ac.id.unikom.codelabs.radio.data.model.TopChart
import kotlinx.coroutines.Deferred
import retrofit2.http.GET


interface TopchartsApiService {
    @GET("program")
    fun getTopcharts(): Deferred<List<TopChart>>
}