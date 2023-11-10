package br.com.painelb.ui.main.fragments.occurrences.create.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.*
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.VehicleConductorFragment

class CreateOccurrencesAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


    override fun getItemCount() = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OccurrenceInformationFragment()
            1 -> VehicleConductorFragment()
            2 -> VictimFragment()
            3 -> WitnessFragment()
            4 -> OccurrenceImageFragment()
            5 -> OccurrencePreviewFragment()
            else -> throw  IllegalArgumentException("Illegal position $position")
        }
    }
}