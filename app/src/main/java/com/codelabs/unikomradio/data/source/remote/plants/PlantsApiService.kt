package com.codelabs.unikomradio.data.source.remote.plants

import com.codelabs.unikomradio.data.model.Plant
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface PlantsApiService {
    @GET("plants.json")
    fun getPlants(): Deferred<List<Plant>>
}