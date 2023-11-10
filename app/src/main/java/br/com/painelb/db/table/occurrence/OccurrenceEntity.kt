package br.com.painelb.db.table.occurrence

import androidx.room.Entity
import androidx.room.Index
import br.com.painelb.model.occurrences.*

@Entity(
    tableName = "occurrence_table",
    primaryKeys = ["id"],
    indices = [Index("id")]
)
data class OccurrenceEntity(
    val id: Long,
    val occurrence: Occurrence,
    val occurreceWitness: List<OccurreceWitnes>,
    val occurrenceVictim: List<OccurrenceVictim>,
    val vehicleConductor: List<VehicleConductor>
) {

    companion object {
        fun from(data: CreateOccurrence) =
            OccurrenceEntity(
                id = data.occurrence.occurrenceId,
                occurrence = data.occurrence,
                occurreceWitness = data.occurreceWitness,
                occurrenceVictim = data.occurrenceVictim,
                vehicleConductor = data.vehicleConductor
            )
    }
}
