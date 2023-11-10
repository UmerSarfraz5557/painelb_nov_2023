package br.com.painelb.domain

import br.com.painelb.prefs.PreferenceStorage
import javax.inject.Inject

open class LevelUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Int, Unit>() {
    override fun execute(parameters: Int) {
        preferenceStorage.level = parameters
    }
}
