package com.codelabs.unikomradio.data.source.remote.plants

import com.codelabs.unikomradio.data.model.TopChart
import kotlinx.coroutines.Deferred
import retrofit2.http.GET


interface TopchartsApiService {
    @GET("program")
    fun getTopcharts(): Deferred<List<TopChart>>
}