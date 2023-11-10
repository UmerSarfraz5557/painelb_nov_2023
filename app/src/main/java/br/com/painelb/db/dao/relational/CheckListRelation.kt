package br.com.painelb.db.dao.relational

import androidx.room.Embedded
import androidx.room.Relation
import br.com.painelb.db.table.checklist.ChecklistEntity
import br.com.painelb.db.table.checklist.ChecklistPhotos
import br.com.painelb.model.checklist.CreateCheckList


data class CheckListRelation(
    @Embedded
    val checklist: ChecklistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "checklistId",
        entity = ChecklistPhotos::class
    )
    val photos: List<ChecklistPhotos> = listOf()
) {
    companion object {
        fun to(data: CheckListRelation) =
            CreateCheckList(
                checklist = data.checklist.checklist,
                checklistPhotos = data.photos
            )
    }
}
