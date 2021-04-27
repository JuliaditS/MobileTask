package ac.id.unikom.codelabs.radio.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "topcharts")
data class TopChart (
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @field:SerializedName("topchartId") val tophartId: Int,
        @field:SerializedName("currentRank") val currentRank: Int,
        @field:SerializedName("rankBefore") val rankBefore: Int,
        @field:SerializedName("song") val song: String,
        @field:SerializedName("singer") val singer: String
){
        constructor() : this(0, 0, 0, "", "")
}