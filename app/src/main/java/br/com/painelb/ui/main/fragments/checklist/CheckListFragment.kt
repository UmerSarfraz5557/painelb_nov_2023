package br.com.painelb.ui.main.fragments.checklist

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentCheckListBinding
import br.com.painelb.model.ItemType
import br.com.painelb.ui.main.fragments.checklist.fragment.adapter.CheckListFragmentStateAdapter
import br.com.painelb.util.navigate


class CheckListFragment : BaseFragment<FragmentCheckListBinding>(R.layout.fragment_check_list) {

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.toolbar.apply {
            inflateMenu(R.menu.checklist)
            setOnMenuItemClickListener { item ->
                when (R.id.action_create_check_list) {
                    item.itemId -> {
                        navigate(
                            CheckListFragmentDirections.navigateToCreateCheckListFragment(
                                itemType = ItemType.NEW
                            )
                        )
                        true
                    }
                    else -> super.onOptionsItemSelected(item)
                }
            }
        }
        mBinding.pager.apply { adapter = CheckListFragmentStateAdapter(this@CheckListFragment) }
    }








}