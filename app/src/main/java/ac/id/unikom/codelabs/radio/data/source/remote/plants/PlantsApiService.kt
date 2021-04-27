package ac.id.unikom.codelabs.radio.data.source.remote.plants

import ac.id.unikom.codelabs.radio.data.model.Plant
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface PlantsApiService {
    @GET("plants.json")
    fun getPlants(): Deferred<List<Plant>>
}