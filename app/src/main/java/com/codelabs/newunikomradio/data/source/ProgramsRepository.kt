package com.codelabs.newunikomradio.data.source

import android.util.Log
import com.codelabs.newunikomradio.data.model.Program
import com.codelabs.newunikomradio.data.source.local.ProgramDao
import com.codelabs.newunikomradio.data.source.remote.ApiService

class ProgramsRepository private constructor(private val programDao: ProgramDao) {

    private val TAG by lazy { ProgramsRepository::class.java.simpleName }

    suspend fun getPrograms(): List<Program> {
        val response = ApiService.programApiService.getPrograms().await()
        try {
            if (response.isNotEmpty()) {
                programDao.insertAll(response)
            }
        } catch (e: Throwable) {
            Log.e(TAG, e.toString())
        }
        return response
    }

    fun getProgram(programId:String) = programDao.getProgram(programId)

    companion object {

        @Volatile private var instance: ProgramsRepository? = null

        fun getInstance(programDao: ProgramDao) =
                instance ?: synchronized(this){
                    instance ?: ProgramsRepository(programDao).also { instance = it }
                }
    }
}