package br.com.painelb.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class AbsentMediatorLiveData<T : Any?> private constructor(): MediatorLiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentMediatorLiveData()
        }
    }
}