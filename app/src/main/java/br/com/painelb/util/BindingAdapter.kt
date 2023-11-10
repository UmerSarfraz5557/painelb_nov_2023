package br.com.painelb.util

import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.R
import br.com.painelb.model.occurrences.OccurreceWitnes
import br.com.painelb.model.occurrences.OccurrenceVictim
import br.com.painelb.model.occurrences.VehicleConductor
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrenceViewModel
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.VehicleConductorListAdapter
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.VictimListAdapter
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.VictimVehicleConductorListAdapter
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.WitnessListAdapter

@BindingAdapter("bind_adapter", "bind_item", requireAll = false)
fun AutoCompleteTextView.bindAdapter(data: Array<String>, item: String?) {
    if (adapter == null && !data.isNullOrEmpty()) {
        val arrayAdapter =
            NonFilterArrayAdapter(this.context, R.layout.simple_dropdown_item_1line, data)
        setAdapter(arrayAdapter)
    }
    if (!item.isNullOrEmpty() && item != text.toString()) {
        setText(item, false)
    }
}

@BindingAdapter("bind_vehicle_conductor", "view_model", "delete_enable", requireAll = false)
fun RecyclerView.bindVehicleConductor(
    data: List<VehicleConductor>?,
    viewModel: CreateOccurrenceViewModel?,
    deleteEnable: Boolean = true
) {
    if (adapter == null) adapter = VehicleConductorListAdapter(viewModel, deleteEnable)

    if (this.adapter is VehicleConductorListAdapter) {
        val listAdapter = this.adapter as VehicleConductorListAdapter
        listAdapter.submitList(data)
        listAdapter.notifyDataSetChanged()
    }
    if (data != null && data.isNotEmpty()) {
        this.clearDecorations()
        this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}

@BindingAdapter("bind_victim", "view_model", "delete_enable", requireAll = false)
fun RecyclerView.bindVictim(
    data: List<OccurrenceVictim>?,
    viewModel: CreateOccurrenceViewModel?,
    deleteEnable: Boolean = true
) {
    if (adapter == null) adapter = VictimListAdapter(viewModel, deleteEnable)

    if (this.adapter is VictimListAdapter) {
        val listAdapter = this.adapter as VictimListAdapter
        listAdapter.submitList(data)
        listAdapter.notifyDataSetChanged()
    }
    if (data != null && data.isNotEmpty()) {
        this.clearDecorations()
        this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}

@BindingAdapter("bind_witness", "view_model", "delete_enable", requireAll = false)
fun RecyclerView.bindWitness(
    data: List<OccurreceWitnes>?,
    viewModel: CreateOccurrenceViewModel?,
    deleteEnable: Boolean = true
) {
    if (adapter == null) adapter = WitnessListAdapter(viewModel, deleteEnable)

    if (this.adapter is WitnessListAdapter) {
        val listAdapter = this.adapter as WitnessListAdapter
        listAdapter.submitList(data)
        listAdapter.notifyDataSetChanged()
    }
    if (data != null && data.isNotEmpty()) {
        this.clearDecorations()
        this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}

@BindingAdapter("bind_victim_vehicle_conductor", "view_model", requireAll = false)
fun RecyclerView.bindWitness(
    data: List<VehicleConductor>?,
    viewModel: CreateOccurrenceViewModel?
) {
    if (adapter == null) adapter = VictimVehicleConductorListAdapter(viewModel)

    if (this.adapter is VictimVehicleConductorListAdapter) {
        val listAdapter = this.adapter as VictimVehicleConductorListAdapter
        listAdapter.submitList(data)
        listAdapter.notifyDataSetChanged()
    }
    if (data != null && data.isNotEmpty()) {
        this.clearDecorations()
        this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}