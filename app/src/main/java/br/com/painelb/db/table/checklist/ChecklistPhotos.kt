package br.com.painelb.db.table.checklist

import androidx.room.Entity
import androidx.room.Index
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "checklist_photo_table",
    primaryKeys = ["photoId"],
    indices = [Index("photoId")]
)
@JsonClass(generateAdapter = true)
data class ChecklistPhotos(
    @Json(name = "checklist_id")
    val checklistId: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "photo")
    val photo: String,
    @Json(name = "photo_id")
    val photoId: Long
)