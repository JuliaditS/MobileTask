package com.codelabs.newunikomradio.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "news")
data class News (
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @field:SerializedName("newsId") val newsId: Int,
        @field:SerializedName("title") val title: String,
        @field:SerializedName("date") val date: String,
        @field:SerializedName("description") val description: String,
        @field:SerializedName("source") val source: String,
        @field:SerializedName("imageUrl") val imageUrl: String
) : Parcelable {
    constructor() : this(0, "", "", "", "","")
}