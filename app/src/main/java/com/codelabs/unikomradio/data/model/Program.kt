package com.codelabs.unikomradio.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "programs")
data class Program (
        @PrimaryKey @ColumnInfo(name = "id") @field:SerializedName("programId") val programId: String,
        @field:SerializedName("imageUrl") val imageUrl: String,
        @field:SerializedName("title") val tite: String,
        @field:SerializedName("dayDate") val dayDate: String,
        @field:SerializedName("startAt") val startAt: String,
        @field:SerializedName("endAt") val endAt: String,
        @field:SerializedName("description") val description: String
        )