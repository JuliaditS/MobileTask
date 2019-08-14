package com.codelabs.unikomradio.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codelabs.unikomradio.data.model.TopChart

@Dao
interface TopchartDao {
    @Query("SELECT * FROM topcharts")
    fun getTopcharts(): LiveData<List<TopChart>>

    @Query("SELECT * FROM plants WHERE id = :topchartId")
    fun getTopchart(topchartId: String): LiveData<TopChart>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topcharts: List<TopChart>)
}