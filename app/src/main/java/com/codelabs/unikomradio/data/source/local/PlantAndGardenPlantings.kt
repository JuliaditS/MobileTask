package com.codelabs.unikomradio.data.source.local

import androidx.room.Embedded
import androidx.room.Relation
import com.codelabs.unikomradio.data.model.GardenPlanting
import com.codelabs.unikomradio.data.model.Plant

class PlantAndGardenPlantings {

    @Embedded
    lateinit var plant: Plant

    @Relation(parentColumn = "id", entityColumn = "plant_id")
    var gardenPlantings: List<GardenPlanting> = arrayListOf()

}