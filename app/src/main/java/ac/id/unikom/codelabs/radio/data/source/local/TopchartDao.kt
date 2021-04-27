package ac.id.unikom.codelabs.radio.data.source.local

import ac.id.unikom.codelabs.radio.data.model.TopChart
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopchartDao {
    @Query("SELECT * FROM topcharts")
    fun getTopcharts(): LiveData<List<TopChart>>

    @Query("SELECT * FROM topcharts WHERE id = :topchartId")
    fun getTopchart(topchartId: String): LiveData<TopChart>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topcharts: List<TopChart>)
}