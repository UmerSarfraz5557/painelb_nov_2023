package br.com.painelb.domain

import br.com.painelb.prefs.PreferenceStorage
import javax.inject.Inject

open class MainUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Boolean, Unit>() {
    override fun execute(parameters: Boolean) {
        preferenceStorage.main = parameters

    }
}
