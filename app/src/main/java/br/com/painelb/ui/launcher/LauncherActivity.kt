package br.com.painelb.ui.launcher

import android.os.Bundle
import br.com.painelb.R
import br.com.painelb.base.ui.BaseActivity
import br.com.painelb.databinding.ActivityLauncherBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.LaunchDestination
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.ui.authentication.AuthenticationActivityScreen
import br.com.painelb.ui.main.MainActivityScreen
import br.com.painelb.util.checkAllMatched
import br.com.painelb.util.viewModelProvider
import timber.log.Timber
import javax.inject.Inject

class LauncherActivity : BaseActivity<ActivityLauncherBinding>(R.layout.activity_launcher) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: LaunchViewModel = viewModelProvider(viewModelFactory)
        viewModel.destination.observe(this, EventObserver {
            Timber.d(it.name)
            when (it) {
                LaunchDestination.MAIN -> activityScreenSwitcher().open(MainActivityScreen(true))
                else -> activityScreenSwitcher().open(
                    AuthenticationActivityScreen(true)
                )
            }.checkAllMatched
        })
    }
}
