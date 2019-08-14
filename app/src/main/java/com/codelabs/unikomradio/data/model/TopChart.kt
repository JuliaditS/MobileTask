package com.codelabs.unikomradio.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "topcharts")
data class TopChart (
        @PrimaryKey @ColumnInfo(name = "id") @field:SerializedName("topchartId") val tophartId: String,
        @field:SerializedName("rank") val rank: Int,
        @field:SerializedName("song") val song: String,
        @field:SerializedName("singer") val singer: String
)