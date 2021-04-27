package ac.id.unikom.codelabs.radio.data.source

import ac.id.unikom.codelabs.radio.data.model.Plant
import ac.id.unikom.codelabs.radio.data.source.local.PlantDao
import ac.id.unikom.codelabs.radio.data.source.remote.ApiService
import android.util.Log

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