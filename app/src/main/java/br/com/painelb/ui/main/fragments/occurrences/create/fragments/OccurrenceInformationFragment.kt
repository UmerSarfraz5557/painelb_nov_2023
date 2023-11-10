package br.com.painelb.ui.main.fragments.occurrences.create.fragments

import android.os.Bundle
import android.view.View
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentOccurrenceInformationBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrenceViewModel
import br.com.painelb.util.parentViewModelProvider
import javax.inject.Inject

class OccurrenceInformationFragment :
    BaseFragment<FragmentOccurrenceInformationBinding>(R.layout.fragment_occurrence_information) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var viewModel: CreateOccurrenceViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = parentViewModelProvider(viewModelFactory)
        mBinding.viewModel = viewModel
    }
}
