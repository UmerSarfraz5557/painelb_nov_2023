package br.com.painelb.util

import androidx.lifecycle.MutableLiveData
import br.com.painelb.domain.*
import br.com.painelb.network.Resource
import br.com.painelb.network.Status
import timber.log.Timber
import javax.inject.Inject

class LogoutHelper @Inject constructor(
    private val appExecutors: AppExecutors,
    private val tokenUseCase: TokenUseCase,
    private val nameUseCase: NameUseCase,
    private val userIdUseCase: UserIdUseCase,
    private val emailUseCase: EmailUseCase,
    private val mainUseCase: MainUseCase
) {
    private val logoutStatus = MutableLiveData<Resource<String>>()
    fun logout(): MutableLiveData<Resource<String>> {
        logoutStatus.value = Resource.loading()
        appExecutors.diskIO().execute {
            tokenUseCase(String.EMPTY)
            nameUseCase(String.EMPTY)
            userIdUseCase(0)
            emailUseCase(String.EMPTY)
            mainUseCase(false)
            appExecutors.mainThread().execute {
                logoutStatus.value = Resource.success(Status.SUCCESS.name)
            }
        }
        return logoutStatus
    }
}
