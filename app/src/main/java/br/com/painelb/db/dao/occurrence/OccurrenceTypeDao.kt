package br.com.painelb.db.dao.occurrence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import br.com.painelb.db.dao.base.BaseDao
import br.com.painelb.db.table.occurrence.OccurrenceType

@Dao
abstract class OccurrenceTypeDao : BaseDao<OccurrenceType> {

    @Query("SELECT * FROM occurrence_type")
    abstract fun getOccurrenceTypeLiveData(): LiveData<List<OccurrenceType>>

}