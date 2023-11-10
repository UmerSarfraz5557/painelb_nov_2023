package br.com.painelb.db.table.occurrence

import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
@Entity(primaryKeys = ["id"], tableName = "vehicle_type")
data class VehicleType(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)