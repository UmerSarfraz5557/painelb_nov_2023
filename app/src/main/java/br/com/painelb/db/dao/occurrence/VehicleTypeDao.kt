package br.com.painelb.db.dao.occurrence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import br.com.painelb.db.dao.base.BaseDao
import br.com.painelb.db.table.occurrence.OccurrenceType
import br.com.painelb.db.table.occurrence.VehicleType


@Dao
abstract class VehicleTypeDao : BaseDao<VehicleType> {

    @Query("SELECT * FROM vehicle_type")
    abstract fun getVehicleTypeLiveData(): LiveData<List<VehicleType>>

}