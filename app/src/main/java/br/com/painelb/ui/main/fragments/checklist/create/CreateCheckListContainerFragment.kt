package br.com.painelb.ui.main.fragments.checklist.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import bd.gov.epwdict.util.onPageSelected
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentCreateCheckListContainerBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.model.ItemType
import br.com.painelb.ui.main.fragments.checklist.create.adapter.CheckListAdapter
import br.com.painelb.ui.main.fragments.checklist.fragment.sent.CheckListSentFragment
import br.com.painelb.util.navigateUp
import br.com.painelb.util.positiveButton
import br.com.painelb.util.showDialog
import javax.inject.Inject

class CreateCheckListContainerFragment :
    BaseFragment<FragmentCreateCheckListContainerBinding>(R.layout.fragment_create_check_list_container) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val checkListViewModel by viewModels<CreateCheckListViewModel> { viewModelFactory }

    private val args: CreateCheckListContainerFragmentArgs by navArgs()

    var id: Long = -1L
    lateinit var itemType: ItemType

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         id = args.Id
         itemType = args.itemType
        if (id != -1L) checkListViewModel.updateFormId(id, itemType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = checkListViewModel

        if (id != -1L) getToolbar()?.setTitle(R.string.title_fragment_update_check_list)
        mBinding.pager.apply {
            val checkListAdapter =
                CheckListAdapter(
                    this@CreateCheckListContainerFragment
                )
            adapter = checkListAdapter
            isUserInputEnabled = false
            checkListViewModel.totalPageCount.value = checkListAdapter.itemCount
            onPageSelected { position -> checkListViewModel.currentQuizPosition.value = position }
        }
        checkListViewModel.messageEvent.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog {
                    setMessage(it)
                    positiveButton(context.getString(R.string.ok))
                }
            }
        })
        checkListViewModel.pagePositionChangeListener.observe(viewLifecycleOwner, EventObserver {
            mBinding.pager.currentItem = it
        })

        checkListViewModel.checklistSaveMessage.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog(cancelable = false, cancelableTouchOutside = false) {
                    setMessage(it)
                    CheckListSentFragment.toReloadData = true
                    positiveButton(context.getString(R.string.ok), handleClick = { navigateUp() })
                }
            }
        })
        checkListViewModel.successMessageEvent.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog(cancelable = false, cancelableTouchOutside = false) {
                    setMessage(it)
                    CheckListSentFragment.toReloadData = true
                    positiveButton(context.getString(R.string.ok), handleClick = { navigateUp() })
                }
            }
        })
    }
}
