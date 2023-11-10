package br.com.painelb.ui.main.fragments.checklist.fragment.pending

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.painelb.domain.result.Event
import br.com.painelb.model.checklist.Checklist
import br.com.painelb.model.occurrences.Occurrence
import br.com.painelb.network.Resource
import br.com.painelb.util.AppExecutors
import javax.inject.Inject

class CheckListPendingViewModel @Inject constructor(
    private val repository: CheckListPendingRepository,
    private val appExecutors: AppExecutors
) : ViewModel() {

    val checkListItemLiveData = repository.checklist()
    val messageEvent = MutableLiveData<Event<String>>()
    val deleteCheckList = MediatorLiveData<Resource<String>>()

    val navigateToUpdate = MutableLiveData<Event<Long>>()

    fun update(item: Checklist) {
        navigateToUpdate.value = Event(item.checklistId)
    }

    fun delete(item: Checklist) {
        deleteCheckList.value = Resource.loading()
        appExecutors.diskIO().execute {
            repository.delete(item.checklistId)
            appExecutors.mainThread().execute {
                deleteCheckList.value = Resource.success("Delete successfully")
            }
        }
    }
}
