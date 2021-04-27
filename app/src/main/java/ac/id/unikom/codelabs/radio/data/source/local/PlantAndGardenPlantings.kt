package ac.id.unikom.codelabs.radio.data.source.local

import ac.id.unikom.codelabs.radio.data.model.GardenPlanting
import ac.id.unikom.codelabs.radio.data.model.Plant
import androidx.room.Embedded
import androidx.room.Relation

class PlantAndGardenPlantings {

    @Embedded
    lateinit var plant: Plant

    @Relation(parentColumn = "id", entityColumn = "plant_id")
    var gardenPlantings: List<GardenPlanting> = arrayListOf()

}