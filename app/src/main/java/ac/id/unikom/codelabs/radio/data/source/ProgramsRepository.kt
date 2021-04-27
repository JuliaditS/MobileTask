package ac.id.unikom.codelabs.radio.data.source

import ac.id.unikom.codelabs.radio.data.model.Program
import ac.id.unikom.codelabs.radio.data.source.local.ProgramDao
import ac.id.unikom.codelabs.radio.data.source.remote.ApiService
import android.util.Log

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