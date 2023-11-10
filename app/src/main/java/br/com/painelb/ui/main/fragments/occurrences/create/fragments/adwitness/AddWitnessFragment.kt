package br.com.painelb.ui.main.fragments.occurrences.create.fragments.adwitness

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentAddWitnessBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.util.navigateUp
import br.com.painelb.util.viewModelProvider
import javax.inject.Inject

class AddWitnessFragment : BaseFragment<FragmentAddWitnessBinding>(R.layout.fragment_add_witness) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var viewModel: AddWitnessViewModel

    private val args: AddWitnessFragmentArgs by navArgs()

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
        viewModel.occurrenceId = args.occurrenceId
        viewModel.setOccurreceWitnes(args.occurreceWitnes)
        mBinding.viewModel = viewModel
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        viewModel.navigateToCreateWitness.observe(viewLifecycleOwner, EventObserver {
            savedStateHandle?.set(EXTRA_ARGUMENT_ADD_WITNESS, it)
            navigateUp()
        })

        viewModel.navigateToUpdateWitness.observe(viewLifecycleOwner, EventObserver {
            savedStateHandle?.set(EXTRA_ARGUMENT_UPDATE_WITNESS, it)
            navigateUp()
        })
    }

    companion object {
        const val EXTRA_ARGUMENT_ADD_WITNESS = "EXTRA_ARGUMENT_ADD_WITNESS"
        const val EXTRA_ARGUMENT_UPDATE_WITNESS = "EXTRA_ARGUMENT_UPDATE_WITNESS"
    }
}