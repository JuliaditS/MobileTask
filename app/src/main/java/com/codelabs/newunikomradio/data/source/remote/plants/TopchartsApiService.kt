package com.codelabs.newunikomradio.data.source.remote.plants

import com.codelabs.newunikomradio.data.model.TopChart
import kotlinx.coroutines.Deferred
import retrofit2.http.GET


interface TopchartsApiService {
    @GET("program")
    fun getTopcharts(): Deferred<List<TopChart>>
}