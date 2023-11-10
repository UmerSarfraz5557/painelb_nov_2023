package br.com.painelb.domain

import br.com.painelb.prefs.PreferenceStorage
import br.com.painelb.util.checkAllMatched
import javax.inject.Inject

open class LaunchUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Unit, LaunchDestination>() {
    override fun execute(parameters: Unit): LaunchDestination = (
            when {
                preferenceStorage.main -> LaunchDestination.MAIN
                else -> LaunchDestination.LOGIN
            }.checkAllMatched)
}
