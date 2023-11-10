package br.com.painelb.ui.main.fragments.occurrences.create.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentOccurrencePreviewBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrenceViewModel
import br.com.painelb.util.parentViewModelProvider
import droidninja.filepicker.utils.GridSpacingItemDecoration
import javax.inject.Inject

class OccurrencePreviewFragment :
    BaseFragment<FragmentOccurrencePreviewBinding>(R.layout.fragment_occurrence_preview) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var viewModel: CreateOccurrenceViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = parentViewModelProvider(viewModelFactory)
        mBinding.viewModel = viewModel
        val spanCunt = 3
        val imageAdapter = ImageListAdapter(requireContext(), viewModel, false, spanCunt)
        val manager = StaggeredGridLayoutManager(spanCunt, StaggeredGridLayoutManager.VERTICAL)
        val spacing = requireContext().resources.getDimensionPixelSize(R.dimen.spacing_normal)
        val decoration = GridSpacingItemDecoration(spanCunt, spacing, false)

        mBinding.imageRecyclerView.apply {
            layoutManager = manager
            adapter = imageAdapter
            addItemDecoration(decoration)
        }
        viewModel.photos.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            imageAdapter.submitList(it)
            imageAdapter.notifyDataSetChanged()
        })
    }
}
