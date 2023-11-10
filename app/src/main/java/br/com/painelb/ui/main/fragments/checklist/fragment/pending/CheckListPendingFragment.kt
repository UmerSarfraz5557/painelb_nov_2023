package br.com.painelb.ui.main.fragments.checklist.fragment.pending

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentCheckListPendingBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.model.ItemType
import br.com.painelb.ui.main.fragments.checklist.CheckListFragmentDirections
import br.com.painelb.util.dividerItemDecorationVertical
import br.com.painelb.util.navigate
import br.com.painelb.util.positiveButton
import br.com.painelb.util.showDialog
import javax.inject.Inject


class CheckListPendingFragment :
    BaseFragment<FragmentCheckListPendingBinding>(R.layout.fragment_check_list_pending) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<CheckListPendingViewModel> { viewModelFactory }

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
        val checkListAdapter = CheckListPendingAdapter(viewModel)
        mBinding.recyclerView.apply {
            adapter = checkListAdapter
            addItemDecoration(context.dividerItemDecorationVertical)
        }

        viewModel.checkListItemLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                checkListAdapter.submitList(it) {
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
            navigate(CheckListFragmentDirections.navigateToCreateCheckListFragment(it, ItemType.LOCAL))
        })
    }
}