package br.com.painelb.db.dao.checklist

import androidx.room.Dao
import androidx.room.Query
import br.com.painelb.db.dao.base.BaseDao
import br.com.painelb.db.table.checklist.ChecklistPhotos

@Dao
abstract class CheckListPhotoDao : BaseDao<ChecklistPhotos> {

    @Query("DELETE FROM checklist_photo_table WHERE checklistId = :id")
    abstract fun deleteByChecklistId(id: Long)

    @Query("DELETE FROM checklist_photo_table")
    abstract fun clear()
}


