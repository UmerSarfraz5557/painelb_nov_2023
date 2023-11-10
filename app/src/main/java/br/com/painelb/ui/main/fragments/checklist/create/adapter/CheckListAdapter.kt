package br.com.painelb.ui.main.fragments.checklist.create.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.painelb.ui.main.fragments.checklist.create.fragment.CreateCheckListFragment
import br.com.painelb.ui.main.fragments.checklist.create.fragment.ImageFragment
import br.com.painelb.ui.main.fragments.checklist.create.fragment.PreviewFragment
import br.com.painelb.ui.main.fragments.checklist.create.fragment.VehicleFragment

class CheckListAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VehicleFragment()
            1 -> CreateCheckListFragment()
            2 -> ImageFragment()
            3 -> PreviewFragment()
            else -> throw  IllegalArgumentException("Illegal position $position")
        }
    }
}