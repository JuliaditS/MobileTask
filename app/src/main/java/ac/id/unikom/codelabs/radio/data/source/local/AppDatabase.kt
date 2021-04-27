package ac.id.unikom.codelabs.radio.data.source.local

import ac.id.unikom.codelabs.radio.data.model.GardenPlanting
import ac.id.unikom.codelabs.radio.data.model.Plant
import ac.id.unikom.codelabs.radio.data.model.TopChart
import ac.id.unikom.codelabs.radio.data.utils.Converters
import ac.id.unikom.codelabs.radio.utilities.DATABASE_NAME
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

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