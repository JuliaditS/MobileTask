package com.codelabs.unikomradio.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "banners")
data class Banner (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @field:SerializedName("bannerId") val bannerId: Int,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("imageUrl") val imageUrl: String
) {
    constructor() : this(0,"", "")
}