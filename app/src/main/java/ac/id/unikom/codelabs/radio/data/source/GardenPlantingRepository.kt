package ac.id.unikom.codelabs.radio.data.source

import ac.id.unikom.codelabs.radio.data.model.GardenPlanting
import ac.id.unikom.codelabs.radio.data.source.local.GardenPlantingDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class GardenPlantingRepository private constructor(
    private val gardenPlantingDao: GardenPlantingDao
){

    suspend fun createGardenPlanting(plantId:String){
        withContext(IO){
            val gardenPlanting = GardenPlanting(plantId)
            gardenPlantingDao.insertGardenPlanting(gardenPlanting)
        }
    }

    suspend fun removeGardenPlanting(gardenPlanting: GardenPlanting){
        withContext(IO){
            gardenPlantingDao.deleteGardenPlanting(gardenPlanting)
        }
    }

    fun getGardenPlantingForPlant(plantId: String) = gardenPlantingDao.getGardenPlantingForPlant(plantId)

    fun getGardenPlantings() = gardenPlantingDao.getGardenPlantings()

    fun getPlantAndGardenPlantings() = gardenPlantingDao.getPlantAndGardenPlantings()

    companion object {

        // for singleton instantiation
        @Volatile private var instance: GardenPlantingRepository? = null

        fun getInstance(gardenPlantingDao: GardenPlantingDao) =
            instance ?: synchronized(this){
                instance ?: GardenPlantingRepository(gardenPlantingDao).also { instance = it }
            }
    }
}