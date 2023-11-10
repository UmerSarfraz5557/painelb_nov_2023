package br.com.painelb.ui.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import br.com.painelb.domain.result.Result
import br.com.painelb.domain.LaunchDestination
import br.com.painelb.domain.LaunchUseCase
import br.com.painelb.domain.result.Event
import timber.log.Timber
import javax.inject.Inject

class LaunchViewModel @Inject constructor(
    launchUseCase: LaunchUseCase
) : ViewModel() {
    private val launchCompletedResult = MutableLiveData<Result<LaunchDestination>>()
    val destination: LiveData<Event<LaunchDestination>>

    init {
        launchUseCase(Unit, launchCompletedResult)
        destination = launchCompletedResult.map {
            Timber.d(it.toString())
            val success = (it as? Result.Success)?.data
            if (success != null) {
                Event(success)
            } else {
                Event(LaunchDestination.LOGIN)
            }
        }
    }
}
