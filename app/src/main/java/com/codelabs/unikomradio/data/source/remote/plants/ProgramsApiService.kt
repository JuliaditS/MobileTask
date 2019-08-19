package com.codelabs.unikomradio.data.source.remote.plants

import com.codelabs.unikomradio.data.model.Program
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ProgramsApiService {
    @GET("program")
    fun getPrograms(): Deferred<List<Program>>
}