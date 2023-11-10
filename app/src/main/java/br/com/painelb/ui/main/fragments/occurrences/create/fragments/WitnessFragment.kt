package br.com.painelb.ui.main.fragments.occurrences.create.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentWitnessBinding
import br.com.painelb.databinding.ListItemWitnessBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.model.occurrences.OccurreceWitnes
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrenceViewModel
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrencesFragmentDirections
import br.com.painelb.util.layoutInflater
import br.com.painelb.util.navigate
import br.com.painelb.util.parentViewModelProvider
import javax.inject.Inject

class WitnessFragment : BaseFragment<FragmentWitnessBinding>(R.layout.fragment_witness) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var viewModel: CreateOccurrenceViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = parentViewModelProvider(viewModelFactory)
        mBinding.viewModel = viewModel
        viewModel.editWitness.observe(viewLifecycleOwner, EventObserver {
            navigate(
                CreateOccurrencesFragmentDirections.navigateToAddWitnessFragment(
                    it.first,
                    it.second
                )
            )
        })
    }
}

class WitnessListAdapter(
    private val viewModel: CreateOccurrenceViewModel?,
    private val deleteEnable: Boolean
) :
    ListAdapter<OccurreceWitnes, WitnessListAdapter.ViewHolder>(DistrictDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemWitnessBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, deleteEnable)
    }

    class ViewHolder(private val binding: ListItemWitnessBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            occurrenceWitness: OccurreceWitnes,
            viewModel: CreateOccurrenceViewModel?,
            deleteEnable: Boolean
        ) {
            binding.item = occurrenceWitness
            binding.deleteImageView.visibility = if (deleteEnable) View.VISIBLE else View.GONE
            binding.editImageView.visibility = if (deleteEnable) View.VISIBLE else View.GONE
            binding.deleteImageView.setOnClickListener { viewModel?.showWitnessDeleteConfirmation(adapterPosition) }
            binding.editImageView.setOnClickListener { viewModel?.onEditWitness(adapterPosition) }
            binding.executePendingBindings()
        }
    }

    object DistrictDiff : DiffUtil.ItemCallback<OccurreceWitnes>() {
        override fun areItemsTheSame(oldItem: OccurreceWitnes, newItem: OccurreceWitnes) =
            oldItem.witnessId == newItem.witnessId

        override fun areContentsTheSame(oldItem: OccurreceWitnes, newItem: OccurreceWitnes) =
            oldItem == newItem
    }
}