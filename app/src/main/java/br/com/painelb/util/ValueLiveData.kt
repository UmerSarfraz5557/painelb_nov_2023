package br.com.painelb.util

import androidx.lifecycle.LiveData

/**
 * A LiveData class that has  value.
 */
class ValueLiveData<T : Any?> private constructor(data: T) : LiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(data)
    }

    companion object {
        fun <T> create(data: T): LiveData<T> {
            return ValueLiveData(data)
        }
    }
}
