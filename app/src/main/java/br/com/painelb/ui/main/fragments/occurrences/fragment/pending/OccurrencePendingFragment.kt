package br.com.painelb.ui.main.fragments.occurrences.fragment.pending

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentOccurrencePendingBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.model.ItemType
import br.com.painelb.ui.main.fragments.occurrences.OccurrencesFragmentDirections
import br.com.painelb.util.dividerItemDecorationVertical
import br.com.painelb.util.navigate
import br.com.painelb.util.positiveButton
import br.com.painelb.util.showDialog
import javax.inject.Inject


class OccurrencePendingFragment :
    BaseFragment<FragmentOccurrencePendingBinding>(R.layout.fragment_occurrence_pending) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<OccurrencesPendingViewModel> { viewModelFactory }

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
        val occurrenceAdapter = OccurrencePendingAdapter(viewModel)
        mBinding.recyclerView.apply {
            adapter = occurrenceAdapter
            addItemDecoration(context.dividerItemDecorationVertical)
        }

        viewModel.occurrenceItemLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                occurrenceAdapter.submitList(it) {
                    val layoutManager =
                        (mBinding.recyclerView.layoutManager as LinearLayoutManager)
                    val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION) mBinding.recyclerView.scrollToPosition(
                        position
                    )
                }
            }
        })

        viewModel.navigateToUpdate.observe(viewLifecycleOwner, EventObserver {
            navigate(OccurrencesFragmentDirections.navigateToCreateOccurrencesFragment(it, ItemType.LOCAL))
        })
    }
}
