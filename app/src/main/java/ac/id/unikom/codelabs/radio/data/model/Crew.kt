package ac.id.unikom.codelabs.radio.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "crews")
data class Crew (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @field:SerializedName("crewId") val crewId: Int,
    @field:SerializedName("userPhoto") val userPhoto: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("role") val role: String
) : Parcelable {
    constructor() : this(0,"", "", "")
}