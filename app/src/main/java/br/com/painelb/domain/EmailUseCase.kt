package br.com.painelb.domain

import br.com.painelb.prefs.PreferenceStorage
import javax.inject.Inject

open class EmailUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<String, Unit>() {
    override fun execute(parameters: String) {
        preferenceStorage.email = parameters
    }
}
