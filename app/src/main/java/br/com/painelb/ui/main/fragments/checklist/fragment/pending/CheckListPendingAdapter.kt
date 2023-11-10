package br.com.painelb.ui.main.fragments.checklist.fragment.pending

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.databinding.ListItemCheckListPendingBinding
import br.com.painelb.databinding.ListItemPendingOccurrenceBinding
import br.com.painelb.model.checklist.Checklist
import br.com.painelb.model.occurrences.Occurrence
import br.com.painelb.util.layoutInflater

class CheckListPendingAdapter(private val sentViewModel: CheckListPendingViewModel) :
    ListAdapter<Checklist, CheckListPendingAdapter.ViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemCheckListPendingBinding.inflate(parent.layoutInflater, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), sentViewModel)

    class ViewHolder(private val binding: ListItemCheckListPendingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            checklistItem: Checklist,
            checklistViewModel: CheckListPendingViewModel
        ) {
            binding.apply {
                viewModel = checklistViewModel
                item = checklistItem
                executePendingBindings()
            }
        }
    }

    internal class DiffCallback : DiffUtil.ItemCallback<Checklist>() {
        override fun areItemsTheSame(oldItem: Checklist, newItem: Checklist) =
            oldItem.checklistId == newItem.checklistId

        override fun areContentsTheSame(oldItem: Checklist, newItem: Checklist) =
            oldItem == newItem
    }
}
