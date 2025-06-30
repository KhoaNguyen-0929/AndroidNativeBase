package vn.start.model

import com.google.gson.annotations.SerializedName

data class DemoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("duration_minutes")
    val durationMinutes: Int
) {
}