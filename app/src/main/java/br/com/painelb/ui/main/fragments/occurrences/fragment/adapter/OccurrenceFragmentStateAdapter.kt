package br.com.painelb.ui.main.fragments.occurrences.fragment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.painelb.ui.main.fragments.occurrences.fragment.pending.OccurrencePendingFragment
import br.com.painelb.ui.main.fragments.occurrences.fragment.sent.OccurrenceSentFragment

class OccurrenceFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OccurrenceSentFragment()
            1 -> OccurrencePendingFragment()
            else -> throw  IllegalArgumentException("Illegal position $position")
        }
    }
}