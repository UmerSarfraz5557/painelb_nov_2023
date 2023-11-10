package br.com.painelb.ui.main.fragments.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentHomeBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.ui.authentication.AuthenticationActivityScreen
import br.com.painelb.util.navigate
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel
        mBinding.toolbar.apply {
            inflateMenu(R.menu.home)
            setOnMenuItemClickListener { item ->
                when (R.id.action_logout) {
                    item.itemId -> {
                        viewModel.logout()
                        true
                    }
                    else -> super.onOptionsItemSelected(item)
                }
            }
        }
        viewModel.navigateToLogoutDialog.observe(viewLifecycleOwner, EventObserver {
            showLogoutDialog()
        })
        viewModel.navigateToAuthScreen.observe(viewLifecycleOwner, EventObserver {
            activityScreenSwitcher()?.open(AuthenticationActivityScreen(true))
        })

        viewModel.navigateToOccurrence.observe(viewLifecycleOwner, EventObserver {
            navigate(HomeFragmentDirections.navigateToOccurrencesFragment())
        })
        viewModel.navigateToCheckList.observe(viewLifecycleOwner, EventObserver {
            navigate(HomeFragmentDirections.navigateToCheckListFragment())
        })
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext()).apply {
            setMessage(R.string.are_you_want_to_logout)
            setPositiveButton(R.string.yes) { _, _ -> viewModel.onConfirmLogout() }
            setNegativeButton(R.string.no) { _, _ -> }
        }
        builder.create().show()
    }
}