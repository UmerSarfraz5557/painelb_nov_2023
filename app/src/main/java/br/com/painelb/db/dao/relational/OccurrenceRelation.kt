package br.com.painelb.db.dao.relational

import androidx.room.Embedded
import androidx.room.Relation
import br.com.painelb.db.table.occurrence.OccurrenceEntity
import br.com.painelb.db.table.occurrence.OccurrencePhoto
import br.com.painelb.model.occurrences.CreateOccurrence


data class OccurrenceRelation(
    @Embedded
    val occurrence: OccurrenceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "occurrenceId",
        entity = OccurrencePhoto::class
    )
    val photos: List<OccurrencePhoto> = listOf()
) {
    companion object {
        fun to(data: OccurrenceRelation) =
            CreateOccurrence(
                occurrence = data.occurrence.occurrence,
                occurreceWitness = data.occurrence.occurreceWitness,
                occurrencePhotos = data.photos,
                occurrenceVictim = data.occurrence.occurrenceVictim,
                vehicleConductor = data.occurrence.vehicleConductor
            )
    }
}
