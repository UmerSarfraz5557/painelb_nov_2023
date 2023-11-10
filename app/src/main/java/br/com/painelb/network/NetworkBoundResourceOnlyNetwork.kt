package br.com.painelb.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import br.com.painelb.api.ApiEmptyResponse
import br.com.painelb.api.ApiErrorResponse
import br.com.painelb.api.ApiResponse
import br.com.painelb.api.ApiSuccessResponse
import br.com.painelb.util.AppExecutors


abstract class NetworkBoundResourceOnlyNetwork<ResultType, T>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        setValue(Resource.loading(null))
        fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        val data = processResponse(response)
                        this.save(Resource.success(data))
                        appExecutors.mainThread().execute {
                            setValue(Resource.success(data))
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        setValue(Resource.success(null))
                    }
                }
                is ApiErrorResponse -> {
                    appExecutors.mainThread().execute {
                        onFetchFailed()
                        setValue(Resource.error(response.errorMessage, null))
                    }
                }
            }
        }
    }


    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    protected fun onFetchFailed() {
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<ResultType>) = response.body

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<ResultType>>

    @WorkerThread
    protected open fun save(response: Resource<ResultType>) {
    }
}
