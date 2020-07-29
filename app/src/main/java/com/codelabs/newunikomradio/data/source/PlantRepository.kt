package com.codelabs.newunikomradio.data.source

import android.util.Log
import com.codelabs.newunikomradio.data.model.Plant
import com.codelabs.newunikomradio.data.source.local.PlantDao
import com.codelabs.newunikomradio.data.source.remote.ApiService

class PlantRepository private constructor(private val plantDao: PlantDao) {

    private val TAG by lazy { PlantRepository::class.java.simpleName }

    suspend fun getPlants(): List<Plant>{
        val response = ApiService.plantApiService.getPlants().await()
        try {
            if (response.isNotEmpty()){
                plantDao.insertAll(response)
            }
        } catch (e : Throwable){
            Log.e(TAG, e.toString())
        }
        return response
    }

    fun getPlant(plantId: String) = plantDao.getPlant(plantId)

    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int) =
        plantDao.getPlantsWithGrowZoneNumber(growZoneNumber)

    companion object {

        // For singleton instantiation
        @Volatile private var instance: PlantRepository? = null

        fun getInstance(plantDao: PlantDao)=
            instance ?: synchronized(this){
                instance ?: PlantRepository(plantDao).also { instance = it }
            }
    }
}