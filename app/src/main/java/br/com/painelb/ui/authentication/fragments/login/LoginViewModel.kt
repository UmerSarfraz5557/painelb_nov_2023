package br.com.painelb.ui.authentication.fragments.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import br.com.painelb.R
import br.com.painelb.domain.*
import br.com.painelb.domain.result.Event
import br.com.painelb.model.login.LoginData
import br.com.painelb.model.login.LoginResponse
import br.com.painelb.network.Resource
import br.com.painelb.network.Status
import br.com.painelb.util.EMPTY
import br.com.painelb.util.isValidEmailAddress
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val context: Context,
    private val loginRepository: LoginRepository,
    private val tokenUseCase: TokenUseCase,
    private val mainUseCase: MainUseCase,
    private val emailUseCase: EmailUseCase,
    private val nameUseCase: NameUseCase,
    private val userIdUseCase: UserIdUseCase,
    private  val levelUseCase: LevelUseCase
) : ViewModel() {

    var userLoginId = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var errorUserLoginId = MutableLiveData<String>()
    var errorPassword = MutableLiveData<String>()

    val navigateToRegistration = MutableLiveData<Event<Unit>>()
    val navigateToForgetPassword = MutableLiveData<Event<Unit>>()
    val messageEvent = MutableLiveData<Event<String>>()
    val navigateToMainActivity = MutableLiveData<Event<Unit>>()

    private val loginData = MutableLiveData<LoginData>()
    val loginResponse: LiveData<Resource<LoginResponse>> = loginData.switchMap {
        loginRepository.loginUser(it)
    }

    private val loginResponseObserver = Observer<Resource<LoginResponse>> {
        Timber.d(it.status.toString())
        if (it.status == Status.ERROR) {
            messageEvent.postValue(Event(it.message!!))
        } else if (it.status == Status.SUCCESS) {
            val data = it.data!!
            mainUseCase(true)
            tokenUseCase(data.token)
            emailUseCase(data.email)
            nameUseCase(data.name)
            userIdUseCase(data.usersId)
            levelUseCase(data.level)
            Log.d("Login","${data.level}")
            navigateToMainActivity.value=(Event(Unit))
        }
    }

    init {
        errorUserLoginId.value = String.EMPTY
        errorPassword.value = String.EMPTY

        userLoginId.observeForever {
            if (!errorUserLoginId.value.isNullOrBlank()) errorUserLoginId.value = String.EMPTY
        }

        password.observeForever {
            if (!errorPassword.value.isNullOrBlank()) errorPassword.value = String.EMPTY
        }

        loginResponse.observeForever(loginResponseObserver)
    }

    fun getLoginClick() {
        if (userLoginId.value.isNullOrEmpty() || !userLoginId.value.isValidEmailAddress()) {
            errorUserLoginId.value = context.getString(R.string.error_message_email)
        } else if (password.value.isNullOrEmpty()) {
            errorPassword.value = context.getString(R.string.error_message_password)
        } else {
            loginData.value = LoginData(userLoginId.value!!, password.value!!)
        }
    }

    fun forgetPassword() {
        navigateToForgetPassword.value = Event(Unit)
    }

    fun registration() {
        navigateToRegistration.postValue(Event(Unit))
    }

    override fun onCleared() {
        super.onCleared()
        loginResponse.removeObserver(loginResponseObserver)
    }
}
