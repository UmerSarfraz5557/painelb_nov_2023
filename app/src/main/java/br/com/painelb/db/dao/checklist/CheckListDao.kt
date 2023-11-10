package br.com.painelb.db.dao.checklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.painelb.db.dao.base.BaseDao
import br.com.painelb.db.dao.relational.CheckListRelation
import br.com.painelb.db.table.checklist.ChecklistEntity
import br.com.painelb.model.checklist.Checklist

@Dao
abstract class CheckListDao : BaseDao<ChecklistEntity> {

    fun getCheckListEntityDistinctLiveData(): LiveData<List<ChecklistEntity>> =
        getChecklistEntityData().distinctUntilChanged()

    @Transaction
    @Query("SELECT * FROM checklist_table ORDER BY id ASC")
    abstract fun getChecklistEntityData(): LiveData<List<ChecklistEntity>>

    fun getChecklistRelationDistinctLiveData(id: Long): LiveData<CheckListRelation> =
        getChecklistRelation(id).distinctUntilChanged()

    @Transaction
    @Query("SELECT * FROM checklist_table  WHERE id = :id")
    protected abstract fun getChecklistRelation(id: Long): LiveData<CheckListRelation>

    fun getChecklistDataDistinctLiveData(): LiveData<List<Checklist>> =
        getChecklistData().distinctUntilChanged()

    @Transaction
    @Query("SELECT checklist FROM checklist_table ORDER BY id ASC")
    abstract fun getChecklistData(): LiveData<List<Checklist>>

    @Query("DELETE FROM checklist_table WHERE id = :id")
    abstract fun deleteById(id: Long)

    @Query("DELETE FROM checklist_table")
    abstract fun clear()
}
