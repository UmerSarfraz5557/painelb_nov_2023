package br.com.painelb.db.dao.occurrence

import androidx.room.Dao
import androidx.room.Query
import br.com.painelb.db.dao.base.BaseDao
import br.com.painelb.db.table.occurrence.OccurrencePhoto

@Dao
abstract class OccurrencePhotoDao : BaseDao<OccurrencePhoto> {

    @Query("DELETE FROM occurrence_photo_table WHERE occurrenceId = :id")
    abstract fun deleteByOccurrenceId(id: Long)

    @Query("DELETE FROM occurrence_photo_table")
    abstract fun clear()
}


