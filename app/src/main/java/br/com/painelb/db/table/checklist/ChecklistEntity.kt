package br.com.painelb.db.table.checklist

import androidx.room.Entity
import androidx.room.Index
import br.com.painelb.model.checklist.Checklist


@Entity(
    tableName = "checklist_table",
    primaryKeys = ["id"],
    indices = [Index("id")]
)
data class  ChecklistEntity(
    val id :Long,
    val checklist: Checklist
)

