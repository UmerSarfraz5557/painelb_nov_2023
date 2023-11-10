package br.com.painelb.ui.main.fragments.occurrences.create.fragments.addvehicle

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentAddVehicleConductorBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.util.navigateUp
import br.com.painelb.util.viewModelProvider
import javax.inject.Inject

class AddVehicleConductorFragment :
    BaseFragment<FragmentAddVehicleConductorBinding>(R.layout.fragment_add_vehicle_conductor) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var viewModel: AddVehicleConductorViewModel

    private val args: AddVehicleConductorFragmentArgs by navArgs()

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
        viewModel.occurrenceId = args.occurrenceId
        viewModel.setVehicleConductor(args.vehicleConductor)

        mBinding.viewModel = viewModel
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        viewModel.navigateToCreateOccurrence.observe(viewLifecycleOwner, EventObserver {
            savedStateHandle?.set(EXTRA_ARGUMENT_ADD_VEHICLE_CONDUCTOR, it)
            navigateUp()
        })
        viewModel.navigateToUpdateOccurrence.observe(viewLifecycleOwner, EventObserver {
            savedStateHandle?.set(EXTRA_ARGUMENT_UPDATE_VEHICLE_CONDUCTOR, it)
            navigateUp()
        })
    }

    companion object {
        const val EXTRA_ARGUMENT_ADD_VEHICLE_CONDUCTOR = "EXTRA_ARGUMENT_ADD_VEHICLE_CONDUCTOR"
        const val EXTRA_ARGUMENT_UPDATE_VEHICLE_CONDUCTOR =
            "EXTRA_ARGUMENT_UPDATE_VEHICLE_CONDUCTOR"
    }
}
