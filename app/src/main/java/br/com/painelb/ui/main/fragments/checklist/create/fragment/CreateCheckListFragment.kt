package br.com.painelb.ui.main.fragments.checklist.create.fragment

import android.os.Bundle
import android.view.View
import android.widget.Spinner
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentCreateCheckListBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.ui.main.fragments.checklist.create.CreateCheckListViewModel
import br.com.painelb.util.parentViewModelProvider
import javax.inject.Inject


class CreateCheckListFragment :
    BaseFragment<FragmentCreateCheckListBinding>(R.layout.fragment_create_check_list) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var createCheckListViewModel: CreateCheckListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createCheckListViewModel = parentViewModelProvider(viewModelFactory)
        mBinding.viewModel = createCheckListViewModel

        val spinner: Spinner = view.findViewById(R.id.spinner_carpet)

        spinner.adapter = createCheckListViewModel.createSpinnerAdapter(requireContext())


    }
}