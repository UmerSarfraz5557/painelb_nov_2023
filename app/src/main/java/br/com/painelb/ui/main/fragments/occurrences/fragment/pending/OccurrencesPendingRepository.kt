package br.com.painelb.ui.main.fragments.occurrences.fragment.pending

import androidx.lifecycle.LiveData
import br.com.painelb.db.dao.occurrence.OccurrenceDao
import br.com.painelb.model.occurrences.Occurrence
import br.com.painelb.util.AppExecutors
import javax.inject.Inject

class OccurrencesPendingRepository @Inject constructor(
    private val occurrenceDao: OccurrenceDao
) {

    fun occurrence(): LiveData<List<Occurrence>> {
        return  occurrenceDao.getOccurrenceDistinctLiveData()
    }

    fun delete(id: Long) {
        occurrenceDao.deleteById(id)
    }
}
