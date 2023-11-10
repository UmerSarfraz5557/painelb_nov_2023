package br.com.painelb.db.dao.occurrence

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.painelb.db.dao.base.BaseDao
import br.com.painelb.db.dao.relational.OccurrenceRelation
import br.com.painelb.db.table.occurrence.OccurrenceEntity
import br.com.painelb.model.occurrences.Occurrence

@Dao
abstract class OccurrenceDao : BaseDao<OccurrenceEntity> {

    fun getOccurrenceEntityDistinctLiveData(): LiveData<List<OccurrenceEntity>> =
        getOccurrenceEntityData().distinctUntilChanged()

    @Transaction
    @Query("SELECT * FROM occurrence_table ORDER BY id ASC")
    abstract fun getOccurrenceEntityData(): LiveData<List<OccurrenceEntity>>

    fun getOccurrenceRelationDistinctLiveData(id: Long): LiveData<OccurrenceRelation> =
        getOccurrenceRelation(id).distinctUntilChanged()

    @Transaction
    @Query("SELECT * FROM occurrence_table  WHERE id = :id")
    protected abstract fun getOccurrenceRelation(id: Long): LiveData<OccurrenceRelation>

    fun getOccurrenceDistinctLiveData(): LiveData<List<Occurrence>> =
        getOccurrenceListData().distinctUntilChanged()

    @Transaction
    @Query("SELECT occurrence FROM occurrence_table ORDER BY id ASC")
    abstract fun getOccurrenceListData(): LiveData<List<Occurrence>>

    @Query("DELETE FROM occurrence_table WHERE id = :id")
    abstract fun deleteById(id: Long)

    @Query("DELETE FROM occurrence_table")
    abstract fun clear()
}
