package br.com.painelb.ui.authentication.fragments.registration

import androidx.lifecycle.LiveData
import br.com.painelb.api.ApiService
import br.com.painelb.model.Response
import br.com.painelb.model.registration.RegistrationData
import br.com.painelb.network.NetworkBoundResourceOnlyNetwork
import br.com.painelb.network.Resource
import br.com.painelb.util.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationRepository @Inject constructor(
    val appExecutors: AppExecutors,
    val apiService: ApiService
) {
    fun registrationUser(user: RegistrationData): LiveData<Resource<Response>> {
        return object : NetworkBoundResourceOnlyNetwork<Response, Any?>(appExecutors) {
            override fun createCall() = apiService.registrationUser(user)
        }.asLiveData()
    }
}
