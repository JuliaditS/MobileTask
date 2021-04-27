package ac.id.unikom.codelabs.radio.data.source.local

import ac.id.unikom.codelabs.radio.data.model.GardenPlanting
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GardenPlantingDao {
    @Query("SELECT * FROM garden_plantings")
    fun getGardenPlantings(): LiveData<List<GardenPlanting>>

    @Query("SELECT * FROM garden_plantings WHERE id = :gardenPlantingId")
    fun getGardenPlanting(gardenPlantingId: Long): LiveData<GardenPlanting>

    @Query("SELECT * FROM garden_plantings WHERE plant_id = :plantId")
    fun getGardenPlantingForPlant(plantId: String): LiveData<GardenPlanting?>

    /**
     * This query will tell Room to query both the [Plants] and [GardenPlanting] tables and handle
     * the object mapping.
     */
    @Transaction
    @Query("SELECT * FROM plants")
    fun getPlantAndGardenPlantings(): LiveData<List<PlantAndGardenPlantings>>

    @Insert
    fun insertGardenPlanting(gardenPlanting: GardenPlanting): Long

    @Delete
    fun deleteGardenPlanting(gardenPlanting: GardenPlanting)
}