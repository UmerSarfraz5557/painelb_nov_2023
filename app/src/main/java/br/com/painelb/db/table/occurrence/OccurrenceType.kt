package br.com.painelb.db.table.occurrence

import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(primaryKeys = ["id"], tableName = "occurrence_type")
data class OccurrenceType(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)