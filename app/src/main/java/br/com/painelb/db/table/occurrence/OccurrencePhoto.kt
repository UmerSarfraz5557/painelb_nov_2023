package br.com.painelb.db.table.occurrence

import androidx.room.Entity
import androidx.room.Index
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "occurrence_photo_table",
    primaryKeys = ["photoId"],
    indices = [Index("photoId")]
)
@JsonClass(generateAdapter = true)
data class OccurrencePhoto(
    @Json(name = "name")
    val name: String,
    @Json(name = "occurrence_id")
    val occurrenceId: Long,
    @Json(name = "photo")
    val photo: String,
    @Json(name = "photo_id")
    val photoId: Long
)