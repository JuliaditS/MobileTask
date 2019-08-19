package com.codelabs.unikomradio.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "program")
data class Program(
    @PrimaryKey @ColumnInfo(name = "id") @field:SerializedName("programId") val programId: Int,
    @field:SerializedName("imageUrl") val imageUrl: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("heldDay") val heldDay: String,
    @field:SerializedName("startAt") val startAt: String,
    @field:SerializedName("endAt") val endAt: String,
    @field:SerializedName("description") val description: String,
    @field:SerializedName("announcer") val announcer: MutableList<Crew>
) : Parcelable {
    constructor() : this(
        0, "", "", "",
        "", "", "", mutableListOf<Crew>()
    )

}
