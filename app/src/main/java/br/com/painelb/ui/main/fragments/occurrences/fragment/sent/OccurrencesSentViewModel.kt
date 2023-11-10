package br.com.painelb.ui.main.fragments.occurrences.fragment.sent

import android.content.Context
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import br.com.painelb.domain.result.Event
import br.com.painelb.model.Response
import br.com.painelb.model.occurrences.CreateOccurrence
import br.com.painelb.network.Resource
import br.com.painelb.network.Status
import br.com.painelb.prefs.PreferenceStorage
import java.text.SimpleDateFormat
import javax.inject.Inject

class OccurrencesSentViewModel @Inject constructor(private val sentRepository: OccurrencesSentRepository,private val sharedPreferenceStorage: PreferenceStorage,private  val context: Context) :
    ViewModel() {


    private val result = MediatorLiveData<Resource<List<CreateOccurrence>>>()
    val occurrenceItemLiveData = MediatorLiveData<List<CreateOccurrence>>()
    val refreshState = MutableLiveData<Status>()
    val messageEvent = MutableLiveData<Event<String>>()
    val navigateToUpdate = MutableLiveData<Event<Long>>()
    val shareEvent = MutableLiveData<Event<String>>()
    val sharePdfEvent = MutableLiveData<Event<String>>()
    val createOccurrence = MutableLiveData<Event<CreateOccurrence>>()
    val navigateToLogoutDialog = MutableLiveData<Event<CreateOccurrence>>()

    private fun occurrencesResultObserver(): Observer<Resource<List<CreateOccurrence>>> {
        return Observer {
            refreshState.value = it.status
            if (it.status == Status.ERROR) {
                messageEvent.value = Event(it.message ?: "Unknown error")
            }
            it.data.let { data ->
                /* commented in 28/12/2021
                if(data != null){
                    Log.d("DateC","Converting")
                    for ((index , occurence ) in data.withIndex()){
                        for ((i ,victim) in occurence.occurrenceVictim.withIndex()){
                            data[index].occurrenceVictim[i].birthDate = dbFormat(victim.birthDate)
                            Log.d("VictimDate","${data[index].occurrenceVictim[i].birthDate}}")
                        }

                    }
                }
                */
                occurrenceItemLiveData.value = data
            }
        }
    }

    val deleteOccurrence = MediatorLiveData<Resource<Response>>()

    private fun deleteOccurrenceObserver(): Observer<Resource<Response>> {
        return Observer {
            when {
                Status.SUCCESS == it.status -> {
                    loadOccurrences()
                    messageEvent.value = Event(it.data?.message ?: "")
                }
                Status.ERROR == it.status -> messageEvent.value = Event(it.message ?: "")
            }
        }
    }

    init {
        loadOccurrences()
        result.observeForever(occurrencesResultObserver())
        deleteOccurrence.observeForever(deleteOccurrenceObserver())
    }

    fun refresh() = loadOccurrences()

    private fun loadOccurrences() {
        val source = sentRepository.occurrence(sharedPreferenceStorage.userId.toString())
        //val source = sentRepository.occurrence()

        result.removeSource(source)
        result.addSource(source) {
               filterData(it)
            if (it.status == Status.ERROR || it.status == Status.SUCCESS)
                result.removeSource(source)
        }
    }

    fun update(item: CreateOccurrence) {
        navigateToUpdate.value = Event(item.occurrence.occurrenceId)
    }

    fun deleteConfirmed(item: CreateOccurrence) {

        val source = sentRepository.delete(item.occurrence.occurrenceId)
        deleteOccurrence.removeSource(source)
        deleteOccurrence.addSource(source) {
            deleteOccurrence.value = it
        }
    }

    fun share(item: CreateOccurrence) {
        createOccurrence.value = Event(item)
        shareEvent.value = Event(item.shareText(context))
        sharePdfEvent.value = Event(item.shareTextForPdf(context))
    }

    override fun onCleared() {
        super.onCleared()
        result.removeObserver(occurrencesResultObserver())
    }

    private fun filterData(it: Resource<List<CreateOccurrence>>) {

        if(sharedPreferenceStorage.level == 2){
            Log.d("AccessLevel","Basic User Occurrence Before Filter ${sharedPreferenceStorage.userId} The size of lits is ${it?.data?.size}")

            var filteredList = it.data?.filter { data-> data.occurrence.usersId == sharedPreferenceStorage.userId }
            Log.d("AccessLevel","Basic User Occurrence After Filter ${sharedPreferenceStorage.userId} The size of lits is ${filteredList?.size}")
            it.data = filteredList
            result?.value = it
        }else{
            Log.d("AccessLevel","Master No filter The size of occurence is ${it.data?.size}")
            result.value = it
        }
    }


    fun dbFormat(date: String): String {
        /* commented in 28/12/2021
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(parser.parse(date))
        */
         return date
    }

    fun delete(item: CreateOccurrence) {
        navigateToLogoutDialog.postValue(Event(item))
    }

}
