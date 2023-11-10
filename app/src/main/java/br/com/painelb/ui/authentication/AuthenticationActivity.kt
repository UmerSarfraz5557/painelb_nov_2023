package br.com.painelb.ui.authentication

import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import br.com.painelb.R
import br.com.painelb.base.ui.BaseActivity
import br.com.painelb.databinding.ActivityAuthenticationBinding


class AuthenticationActivity :
    BaseActivity<ActivityAuthenticationBinding>(R.layout.activity_authentication) {
    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(R.id.login_fragment)
    }

    private val mNavController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(mNavController, appBarConfiguration)
    }
}
