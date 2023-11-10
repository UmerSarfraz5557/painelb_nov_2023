package br.com.painelb.ui.main.fragments.occurrences.fragment.pending

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.databinding.ListItemPendingOccurrenceBinding
import br.com.painelb.model.occurrences.Occurrence
import br.com.painelb.util.layoutInflater

class OccurrencePendingAdapter(private val sentViewModel: OccurrencesPendingViewModel) :
    ListAdapter<Occurrence, OccurrencePendingAdapter.ViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemPendingOccurrenceBinding.inflate(parent.layoutInflater, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), sentViewModel)

    class ViewHolder(private val binding: ListItemPendingOccurrenceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            occurrencesItem: Occurrence,
            occurrencesSentViewModel: OccurrencesPendingViewModel
        ) {
            binding.apply {
                viewModel = occurrencesSentViewModel
                item = occurrencesItem
                executePendingBindings()
            }
        }
    }

    internal class DiffCallback : DiffUtil.ItemCallback<Occurrence>() {
        override fun areItemsTheSame(oldItem: Occurrence, newItem: Occurrence) =
            oldItem.occurrenceId == newItem.occurrenceId

        override fun areContentsTheSame(oldItem: Occurrence, newItem: Occurrence) =
            oldItem == newItem
    }
}
