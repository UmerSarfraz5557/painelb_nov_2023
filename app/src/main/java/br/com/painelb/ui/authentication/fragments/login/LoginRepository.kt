package br.com.painelb.ui.authentication.fragments.login

import androidx.lifecycle.LiveData
import br.com.painelb.api.ApiService
import br.com.painelb.model.login.LoginData
import br.com.painelb.model.login.LoginResponse
import br.com.painelb.network.NetworkBoundResourceOnlyNetwork
import br.com.painelb.network.Resource
import br.com.painelb.util.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    val appExecutors: AppExecutors,
    val apiService: ApiService
) {
    fun loginUser(data: LoginData): LiveData<Resource<LoginResponse>> {
        return object : NetworkBoundResourceOnlyNetwork<LoginResponse, Any?>(appExecutors) {
            override fun createCall() = apiService.loginUser(data)
        }.asLiveData()
    }
}
