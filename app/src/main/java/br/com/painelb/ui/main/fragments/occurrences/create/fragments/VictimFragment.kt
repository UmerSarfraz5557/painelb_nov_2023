package br.com.painelb.ui.main.fragments.occurrences.create.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentVictimBinding
import br.com.painelb.databinding.ListItemVictimBinding
import br.com.painelb.databinding.ListItemVictimVehicleConductorBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.model.occurrences.OccurrenceVictim
import br.com.painelb.model.occurrences.VehicleConductor
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrenceViewModel
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrencesFragmentDirections
import br.com.painelb.util.layoutInflater
import br.com.painelb.util.navigate
import br.com.painelb.util.parentViewModelProvider
import javax.inject.Inject


class VictimFragment : BaseFragment<FragmentVictimBinding>(R.layout.fragment_victim) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var viewModel: CreateOccurrenceViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = parentViewModelProvider(viewModelFactory)
        mBinding.viewModel = viewModel
        viewModel.editVictim.observe(viewLifecycleOwner, EventObserver {
            navigate(
                CreateOccurrencesFragmentDirections.navigateToAddVictimFragment(
                    it.first,
                    it.second
                )
            )
        })
    }
}

class VictimVehicleConductorListAdapter(private val viewModel: CreateOccurrenceViewModel?) :
    ListAdapter<VehicleConductor, VictimVehicleConductorListAdapter.ViewHolder>(DistrictDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemVictimVehicleConductorBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, position)
    }

    class ViewHolder(private val binding: ListItemVictimVehicleConductorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            vehicleConductor: VehicleConductor,
            viewModel: CreateOccurrenceViewModel?,
            position: Int
        ) {
            binding.item = vehicleConductor
            binding.checkbox.setOnClickListener {
                viewModel?.onVictimDriverSelected(
                    binding.checkbox.isChecked,
                    position,
                    vehicleConductor
                ){
                    binding.checkbox.isChecked = it
                }

            }
            binding.executePendingBindings()

        }
    }

    object DistrictDiff : DiffUtil.ItemCallback<VehicleConductor>() {
        override fun areItemsTheSame(oldItem: VehicleConductor, newItem: VehicleConductor) =
            oldItem.vehicleId == newItem.vehicleId

        override fun areContentsTheSame(oldItem: VehicleConductor, newItem: VehicleConductor) =
            oldItem == newItem
    }
}

class VictimListAdapter(
    private val viewModel: CreateOccurrenceViewModel?,
    private val deleteEnable: Boolean
) :
    ListAdapter<OccurrenceVictim, VictimListAdapter.ViewHolder>(DistrictDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemVictimBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, deleteEnable)
    }

    class ViewHolder(private val binding: ListItemVictimBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            occurrenceVictim: OccurrenceVictim,
            viewModel: CreateOccurrenceViewModel?,
            deleteEnable: Boolean
        ) {
            binding.item = occurrenceVictim
            binding.deleteImageView.visibility = if (deleteEnable) View.VISIBLE else View.GONE
            binding.editImageView.visibility = if (deleteEnable) View.VISIBLE else View.GONE
            binding.deleteImageView.setOnClickListener { viewModel?.showVictimDeleteConfirmation(adapterPosition) }
            binding.editImageView.setOnClickListener { viewModel?.onEditVictim(adapterPosition) }
            binding.executePendingBindings()
        }
    }

    object DistrictDiff : DiffUtil.ItemCallback<OccurrenceVictim>() {
        override fun areItemsTheSame(oldItem: OccurrenceVictim, newItem: OccurrenceVictim) =
            oldItem.victimId == newItem.victimId

        override fun areContentsTheSame(oldItem: OccurrenceVictim, newItem: OccurrenceVictim) =
            oldItem == newItem
    }
}
