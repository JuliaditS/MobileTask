package com.codelabs.newunikomradio.data.source.remote.plants

import com.codelabs.newunikomradio.data.model.Program
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ProgramsApiService {
    @GET("program")
    fun getPrograms(): Deferred<List<Program>>
}