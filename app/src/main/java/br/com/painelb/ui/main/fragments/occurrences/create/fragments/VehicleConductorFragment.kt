package br.com.painelb.ui.main.fragments.occurrences.create.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentVehicleConductorBinding
import br.com.painelb.databinding.ListItemVehicleConductorBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.model.occurrences.VehicleConductor
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrenceViewModel
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrencesFragmentDirections
import br.com.painelb.util.layoutInflater
import br.com.painelb.util.navigate
import br.com.painelb.util.parentViewModelProvider
import javax.inject.Inject

class VehicleConductorFragment :
    BaseFragment<FragmentVehicleConductorBinding>(R.layout.fragment_vehicle_conductor) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var viewModel: CreateOccurrenceViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = parentViewModelProvider(viewModelFactory)
        mBinding.viewModel = viewModel
        viewModel.editVehicleConductors.observe(viewLifecycleOwner, EventObserver {
            navigate(
                CreateOccurrencesFragmentDirections.navigateToAddVehicleConductorFragment(
                    it.first,
                    it.second
                )
            )
        })
    }
}

class VehicleConductorListAdapter(
    private val viewModel: CreateOccurrenceViewModel?,
    private val deleteEnable: Boolean
) :
    ListAdapter<VehicleConductor, VehicleConductorListAdapter.ViewHolder>(DistrictDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemVehicleConductorBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, deleteEnable)
    }

    class ViewHolder(private val binding: ListItemVehicleConductorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            vehicleConductor: VehicleConductor,
            viewModel: CreateOccurrenceViewModel?,
            deleteEnable: Boolean
        ) {
            binding.item = vehicleConductor
            binding.deleteImageView.visibility = if (deleteEnable) VISIBLE else GONE
            binding.editImageView.visibility = if (deleteEnable) VISIBLE else GONE
            binding.deleteImageView.setOnClickListener {
                viewModel?.showDeleteConfirmationDialog(adapterPosition)
            }
            binding.editImageView.setOnClickListener {
                viewModel?.onEditVehicleConductor(adapterPosition)
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
