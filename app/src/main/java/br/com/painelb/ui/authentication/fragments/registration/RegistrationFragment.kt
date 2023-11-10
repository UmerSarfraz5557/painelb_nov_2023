package br.com.painelb.ui.authentication.fragments.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentRegistrationBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.util.navigate
import br.com.painelb.util.positiveButton
import br.com.painelb.util.showDialog
import javax.inject.Inject

class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(R.layout.fragment_registration) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<RegistrationViewModel> { viewModelFactory }

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel

        viewModel.messageEvent.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog {
                    setMessage(it)
                    positiveButton(context.getString(R.string.ok))
                }
            }
        })

        viewModel.registrationSuccessEvent.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog {
                    setMessage(it)
                    positiveButton(context.getString(R.string.ok), handleClick = {
                        navigate(RegistrationFragmentDirections.navigateToLoginFragment())
                    })
                }
            }
        })
    }
}
