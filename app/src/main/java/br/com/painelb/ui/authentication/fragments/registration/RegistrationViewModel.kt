package br.com.painelb.ui.authentication.fragments.registration

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import br.com.painelb.R
import br.com.painelb.domain.result.Event
import br.com.painelb.model.Response
import br.com.painelb.model.registration.RegistrationData
import br.com.painelb.network.Resource
import br.com.painelb.network.Status
import br.com.painelb.util.AbsentLiveData
import br.com.painelb.util.EMPTY
import br.com.painelb.util.isValidEmailAddress
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val context: Context,
    private val repository: RegistrationRepository
) : ViewModel() {

    var name = MutableLiveData<String>()
    var errorName = MutableLiveData<String>()

    var email = MutableLiveData<String>()
    var errorEmail = MutableLiveData<String>()

    var password = MutableLiveData<String>()
    var errorPassword = MutableLiveData<String>()

    var confirmPassword = MutableLiveData<String>()
    var errorConfirmPassword = MutableLiveData<String>()

    var cpf = MutableLiveData<String>()
    var errorCpf = MutableLiveData<String>()

    var fixedTeam = MutableLiveData<String>()
    var errorFixedTeam = MutableLiveData<String>()

    var variableTeam = MutableLiveData<String>()
    var errorVariableTeam = MutableLiveData<String>()

    var sector = MutableLiveData<String>()
    var errorSector = MutableLiveData<String>()

    val messageEvent = MutableLiveData<Event<String>>()
    val registrationSuccessEvent = MutableLiveData<Event<String>>()

    private val registrationData = MutableLiveData<RegistrationData>()
    val registrationStatus: LiveData<Resource<Response>> = Transformations
        .switchMap(registrationData) {
            if (it == null) {
                AbsentLiveData.create()
            } else {
                repository.registrationUser(it)
            }
        }

    init {
        errorName.value = String.EMPTY
        errorPassword.value = String.EMPTY
        errorConfirmPassword.value = String.EMPTY
        errorEmail.value = String.EMPTY
        errorCpf.value = String.EMPTY
        errorFixedTeam.value = String.EMPTY
        errorVariableTeam.value = String.EMPTY
        errorSector.value = String.EMPTY

        name.observeForever {
            if (!errorName.value.isNullOrBlank()) errorName.value = String.EMPTY
        }

        password.observeForever {
            if (!errorPassword.value.isNullOrBlank()) errorPassword.value = String.EMPTY
        }

        confirmPassword.observeForever {
            if (!errorConfirmPassword.value.isNullOrBlank()) errorConfirmPassword.value =
                String.EMPTY
        }

        email.observeForever {
            if (!errorEmail.value.isNullOrBlank()) errorEmail.value = String.EMPTY
        }

        cpf.observeForever {
            if (!errorCpf.value.isNullOrBlank()) errorCpf.value = String.EMPTY
        }

        fixedTeam.observeForever {
            if (!errorFixedTeam.value.isNullOrBlank()) errorFixedTeam.value =
                String.EMPTY
        }
        variableTeam.observeForever {
            if (!errorVariableTeam.value.isNullOrBlank()) errorVariableTeam.value =
                String.EMPTY
        }
        sector.observeForever {
            if (!errorSector.value.isNullOrBlank()) errorSector.value =
                String.EMPTY
        }

        registrationStatus.observeForever {
            if (it.status == Status.ERROR) {
                messageEvent.postValue(Event(it.message!!))
            } else if (it.status == Status.SUCCESS) {
                val mgs = it.data?.message
                    ?: context.applicationContext.getString(R.string.error_message_unknown)
                registrationSuccessEvent.postValue(Event(mgs))
            }
        }
    }

    fun getRegistrationClick() {
        if (name.value.isNullOrEmpty()) {
            errorName.value = context.applicationContext.getString(R.string.error_message_name)
        } else if (!email.value.isValidEmailAddress()) {
            errorEmail.value = context.applicationContext.getString(R.string.error_message_email)
        } else if (password.value.isNullOrEmpty()) {
            errorPassword.value =
                context.applicationContext.getString(R.string.error_message_password)
        } else if (password.value!!.length < 6) {
            errorPassword.value =
                context.applicationContext.getString(R.string.minimum_six_characters_password)
        } else if (password.value!!.startsWith(" ", true) || password.value!!.endsWith(" ", true)) {
            errorPassword.value =
                context.applicationContext.getString(R.string.leading_trailing_passwords)
        } else if (!password.value.equals(confirmPassword.value)) {
            errorConfirmPassword.value =
                context.applicationContext.getString(R.string.invalid_confirm_password)
        } else if (cpf.value.isNullOrEmpty()) {
            errorCpf.value =
                context.applicationContext.getString(R.string.invalid_cpf)
        } else if (fixedTeam.value.isNullOrEmpty()) {
            errorFixedTeam.value =
                context.applicationContext.getString(R.string.invalid_fixed_team)
        } else if (variableTeam.value.isNullOrEmpty()) {
            errorVariableTeam.value =
                context.applicationContext.getString(R.string.invalid_variable_team)
        } else if (sector.value.isNullOrEmpty()) {
            errorSector.value =
                context.applicationContext.getString(R.string.invalid_sector)
        } else {
            registrationData.value =
                RegistrationData(
                    name.value!!,
                    password.value!!,
                    email.value!!,
                    System.currentTimeMillis(),
                    cpf.value!!,
                    fixedTeam.value!!,
                    variableTeam.value!!,
                    sector.value!!
                )
        }
    }
}
