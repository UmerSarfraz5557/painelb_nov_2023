package br.com.painelb.ui.main.fragments.checklist.fragment.sent

import androidx.lifecycle.LiveData
import br.com.painelb.api.ApiResponse
import br.com.painelb.api.ApiService
import br.com.painelb.model.Response
import br.com.painelb.model.checklist.CreateCheckList
import br.com.painelb.network.NetworkBoundResourceOnlyNetwork
import br.com.painelb.network.Resource
import br.com.painelb.util.AppExecutors
import javax.inject.Inject

class ChecklistSentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService
) {

    fun checklist(id : String): LiveData<Resource<List<CreateCheckList>>> {
        return object :
            NetworkBoundResourceOnlyNetwork<List<CreateCheckList>, List<CreateCheckList>>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<List<CreateCheckList>>> =
            //    apiService.checklist()
                apiService.checklistByUserId(id)

        }.asLiveData()
    }

    fun delete(id: Long): LiveData<Resource<Response>> {
        return object :
            NetworkBoundResourceOnlyNetwork<Response, Response>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<Response>> =
                apiService.deleteChecklist(id)
        }.asLiveData()
    }
}
