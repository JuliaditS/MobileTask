package com.codelabs.newunikomradio.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codelabs.newunikomradio.data.model.GardenPlanting
import com.codelabs.newunikomradio.data.model.Plant
import com.codelabs.newunikomradio.data.model.TopChart
import com.codelabs.newunikomradio.data.utils.Converters
import com.codelabs.newunikomradio.utilities.DATABASE_NAME

@Database(entities = [GardenPlanting::class, Plant::class, TopChart::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun gardenPlantingDao(): GardenPlantingDao
    abstract fun plantDao(): PlantDao
    abstract fun topchartDao(): TopchartDao

    companion object {

        // For singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}