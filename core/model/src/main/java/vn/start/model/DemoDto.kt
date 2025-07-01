package vn.start.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class DemoDto(
    @SerializedName("id")
    @PrimaryKey
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("duration_minutes")
    val durationMinutes: Int
)