package br.com.painelb.ui.main

import androidx.lifecycle.LiveData
import br.com.painelb.api.ApiResponse
import br.com.painelb.api.ApiService
import br.com.painelb.model.UpdateResponse
import br.com.painelb.model.occurrences.CreateOccurrence
import br.com.painelb.network.NetworkBoundResourceOnlyNetwork
import br.com.painelb.network.Resource
import br.com.painelb.util.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService
) {

     fun update(version : String): LiveData<Resource<UpdateResponse>> {
        return object :
            NetworkBoundResourceOnlyNetwork<UpdateResponse, UpdateResponse>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<UpdateResponse>> =
                    apiService.update(version)
        }.asLiveData()
    }



}
