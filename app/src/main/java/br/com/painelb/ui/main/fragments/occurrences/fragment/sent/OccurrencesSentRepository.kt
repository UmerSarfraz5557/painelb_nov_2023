package br.com.painelb.ui.main.fragments.occurrences.fragment.sent

import androidx.lifecycle.LiveData
import br.com.painelb.api.ApiResponse
import br.com.painelb.api.ApiService
import br.com.painelb.model.Response
import br.com.painelb.model.occurrences.CreateOccurrence
import br.com.painelb.network.NetworkBoundResourceOnlyNetwork
import br.com.painelb.network.Resource
import br.com.painelb.util.AppExecutors
import javax.inject.Inject

class OccurrencesSentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService
) {

    fun occurrence(id : String): LiveData<Resource<List<CreateOccurrence>>> {
        return object :
            NetworkBoundResourceOnlyNetwork<List<CreateOccurrence>, List<CreateOccurrence>>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<List<CreateOccurrence>>> =
                apiService.occurrenceByUserId(id)
                //apiService.occurrence()

        }.asLiveData()
    }

    fun delete(id: Long): LiveData<Resource<Response>> {
        return object :
            NetworkBoundResourceOnlyNetwork<Response, Response>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<Response>> =
                apiService.deleteOccurrence(id)
        }.asLiveData()
    }
}
