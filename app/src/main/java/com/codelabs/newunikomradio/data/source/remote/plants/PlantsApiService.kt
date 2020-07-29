package com.codelabs.newunikomradio.data.source.remote.plants

import com.codelabs.newunikomradio.data.model.Plant
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface PlantsApiService {
    @GET("plants.json")
    fun getPlants(): Deferred<List<Plant>>
}