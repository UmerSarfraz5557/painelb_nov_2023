package br.com.painelb.ui.authentication

import android.content.Intent
import br.com.painelb.base.navigation.ActivityScreen
import br.com.painelb.base.ui.BaseActivity
import kotlin.reflect.KClass

class AuthenticationActivityScreen constructor(private val needClearStack: Boolean) : ActivityScreen() {

    companion object {
        private const val NEED_TO_CLEAR_STACK = "AuthenticationActivityScreen.ClearStack"
    }

    override fun activityClass(): KClass<out BaseActivity<*>> = AuthenticationActivity::class

    override
    fun configureIntent(intent: Intent) {
        if (needClearStack) {
            intent.flags = (Intent.FLAG_ACTIVITY_NO_ANIMATION
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra(NEED_TO_CLEAR_STACK, needClearStack)
        }
    }
}
