package br.com.painelb.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import br.com.painelb.domain.result.Event
import br.com.painelb.model.UpdateResponse
import br.com.painelb.model.occurrences.CreateOccurrence
import br.com.painelb.network.Resource
import br.com.painelb.network.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import javax.inject.Inject
import android.content.pm.PackageManager

import android.content.pm.PackageInfo

class MainViewModel @Inject constructor(val mainRepository: MainRepository , val context : Context) :
    ViewModel() {

    val updateEvent = MutableLiveData<Event<Boolean?>>()
    private val result = MediatorLiveData<Resource<UpdateResponse>>()

    private fun occurrencesResultObserver(): Observer<Resource<UpdateResponse>> {
        return Observer {
            it.data.let { data ->
            }
        }
    }

    init {
        getVersion()

        result.observeForever(occurrencesResultObserver())

    }

    fun update(version : String) {

        Log.d("Update", "Called")
        val source = mainRepository.update(version)
        result.removeSource(source)
        result.addSource(source) {
            it.data.let { update->
                if (update!= null){
                    updateEvent.postValue(Event(update!!.update))
                }

            }
            Log.d("Update","Res"+source.value?.data)
            if (it.status == Status.ERROR || it.status == Status.SUCCESS)
                result.removeSource(source)
        }

    }

    fun getVersion(){
        try {
            val pInfo: PackageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            val version = pInfo.versionCode
            update("${version}");
            Log.d("Version","${version}")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

}