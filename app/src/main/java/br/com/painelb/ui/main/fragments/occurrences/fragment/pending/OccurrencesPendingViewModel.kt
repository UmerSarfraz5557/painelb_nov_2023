package br.com.painelb.ui.main.fragments.occurrences.fragment.pending

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.painelb.domain.result.Event
import br.com.painelb.model.occurrences.Occurrence
import br.com.painelb.network.Resource
import br.com.painelb.util.AppExecutors
import javax.inject.Inject

class OccurrencesPendingViewModel @Inject constructor(
    private val repository: OccurrencesPendingRepository,
    private val appExecutors: AppExecutors
) : ViewModel() {

    val occurrenceItemLiveData = repository.occurrence()
    val messageEvent = MutableLiveData<Event<String>>()
    val deleteOccurrence = MediatorLiveData<Resource<String>>()
    val navigateToUpdate = MutableLiveData<Event<Long>>()

    fun update(item: Occurrence) {
        navigateToUpdate.value = Event(item.occurrenceId)
    }

    fun delete(item: Occurrence) {
        deleteOccurrence.value = Resource.loading()
        appExecutors.diskIO().execute {
            repository.delete(item.occurrenceId)
            appExecutors.mainThread().execute {
                deleteOccurrence.value = Resource.success("Delete successfully")
            }
        }
    }
}
