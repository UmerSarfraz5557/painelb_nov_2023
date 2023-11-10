package br.com.painelb.domain

import br.com.painelb.prefs.PreferenceStorage
import javax.inject.Inject

open class UserIdUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Long, Unit>() {
    override fun execute(parameters: Long) {
        preferenceStorage.userId = parameters
    }
}
