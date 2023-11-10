package br.com.painelb.ui.authentication.fragments.login

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentLoginBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.ui.main.MainActivityScreen
import br.com.painelb.util.navigate
import br.com.painelb.util.positiveButton
import br.com.painelb.util.showDialog
import timber.log.Timber
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: LoginViewModel by navGraphViewModels(R.id.auth_nav_graph) { viewModelFactory }

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel
        viewModel.navigateToRegistration.observe(viewLifecycleOwner, EventObserver {
            navigate(LoginFragmentDirections.navigateToRegistrationFragment())
        })
        viewModel.navigateToForgetPassword.observe(viewLifecycleOwner, EventObserver {
            navigate(LoginFragmentDirections.navigateToForgetPasswordFragment())
        })

        viewModel.messageEvent.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog {
                    setMessage(it)
                    positiveButton(getString(R.string.ok))
                }
            }
        })
        viewModel.navigateToMainActivity.observe(viewLifecycleOwner, EventObserver {
            Timber.d("navigateToMainActivity")
            val mScreen = MainActivityScreen(true)
            activityScreenSwitcher()?.open(mScreen)
            activity?.overridePendingTransition(R.anim.fade, R.anim.hold)
        })
    }
}
