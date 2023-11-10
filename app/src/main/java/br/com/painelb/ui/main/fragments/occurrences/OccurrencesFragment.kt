package br.com.painelb.ui.main.fragments.occurrences

import android.os.Bundle
import android.view.View
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentOccurrencesBinding
import br.com.painelb.model.ItemType
import br.com.painelb.ui.main.fragments.occurrences.fragment.adapter.OccurrenceFragmentStateAdapter
import br.com.painelb.util.navigate

class OccurrencesFragment :
    BaseFragment<FragmentOccurrencesBinding>(R.layout.fragment_occurrences) {

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.toolbar.apply {
            inflateMenu(R.menu.occurrences)
            setOnMenuItemClickListener { item ->
                when (R.id.action_create_occurrences) {
                    item.itemId -> {
                        navigate(OccurrencesFragmentDirections.navigateToCreateOccurrencesFragment(itemType = ItemType.NEW))
                        true
                    }
                    else -> super.onOptionsItemSelected(item)
                }
            }
        }
        mBinding.pager.apply { adapter = OccurrenceFragmentStateAdapter(this@OccurrencesFragment) }
    }
}