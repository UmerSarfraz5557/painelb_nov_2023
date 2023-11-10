package br.com.painelb.ui.main.fragments.checklist.fragment.sent

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.databinding.ListItemCheckListBinding
import br.com.painelb.model.checklist.CreateCheckList
import br.com.painelb.util.layoutInflater

class ChecklistAdapter(private val sentViewModel: CheckListSentViewModel) : ListAdapter<CreateCheckList,
        ChecklistAdapter.ViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemCheckListBinding.inflate(parent.layoutInflater, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), sentViewModel)

    class ViewHolder(private val binding: ListItemCheckListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(checklist: CreateCheckList, checklistSentViewModel: CheckListSentViewModel) {
            binding.apply {
                viewModel = checklistSentViewModel
                item = checklist
                executePendingBindings()
            }
        }
    }

    internal class DiffCallback : DiffUtil.ItemCallback<CreateCheckList>() {
        override fun areItemsTheSame(oldItem: CreateCheckList, newItem: CreateCheckList) =
            oldItem.checklist.checklistId == newItem.checklist.checklistId

        override fun areContentsTheSame(oldItem: CreateCheckList, newItem: CreateCheckList) =
            oldItem == newItem
    }
}
