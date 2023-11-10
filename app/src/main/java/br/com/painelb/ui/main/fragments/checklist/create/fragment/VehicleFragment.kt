package br.com.painelb.ui.main.fragments.checklist.create.fragment

import android.os.Bundle
import android.view.View
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentVehicleBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.ui.main.fragments.checklist.create.CreateCheckListViewModel
import br.com.painelb.util.parentViewModelProvider
import javax.inject.Inject

class VehicleFragment : BaseFragment<FragmentVehicleBinding>(R.layout.fragment_vehicle) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var createCheckListViewModel: CreateCheckListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createCheckListViewModel = parentViewModelProvider(viewModelFactory)
        mBinding.viewModel = createCheckListViewModel

        createCheckListViewModel.resetValue.observe(viewLifecycleOwner, EventObserver {
            mBinding.outputFuelQuantityEditText.setText("")
            mBinding.returnFuelQuantityEditText.setText("")
        })
    }
}
