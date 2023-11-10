package br.com.painelb.ui.main.fragments.checklist.fragment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.painelb.ui.main.fragments.checklist.fragment.pending.CheckListPendingFragment
import br.com.painelb.ui.main.fragments.checklist.fragment.sent.CheckListSentFragment

class CheckListFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CheckListSentFragment()
            1 -> CheckListPendingFragment()
            else -> throw  IllegalArgumentException("Illegal position $position")
        }
    }
}