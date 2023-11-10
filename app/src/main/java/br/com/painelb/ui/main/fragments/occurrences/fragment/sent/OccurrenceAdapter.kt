package br.com.painelb.ui.main.fragments.occurrences.fragment.sent

import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.painelb.R
import br.com.painelb.databinding.ListItemOccurrenceBinding
import br.com.painelb.model.occurrences.CreateOccurrence
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.ImageListAdapter
import br.com.painelb.util.compatRemoveIf
import br.com.painelb.util.executeAfter
import br.com.painelb.util.layoutInflater
import droidninja.filepicker.utils.GridSpacingItemDecoration

class OccurrenceAdapter(private val sentViewModel: OccurrencesSentViewModel, savedState: Bundle?) :
    ListAdapter<CreateOccurrence,
            OccurrenceAdapter.ViewHolder>(
        DiffCallback()
    ) {

    companion object {

        private const val STATE_KEY_EXPANDED_IDS = "CodelabsAdapter:expandedIds"
    }

    private var expandedIds = mutableSetOf<String>()

    init {
        savedState?.getStringArray(STATE_KEY_EXPANDED_IDS)?.let {
            expandedIds.addAll(it)
        }
    }

    fun onSaveInstanceState(state: Bundle) {
        state.putStringArray(STATE_KEY_EXPANDED_IDS, expandedIds.toTypedArray())
    }

    override fun submitList(list: List<CreateOccurrence>?) {
        // Clear out any invalid IDs

        if (list == null) {
            expandedIds.clear()
        } else {
            val ids = list.map { it.occurrence.occurrenceId.toString() }
            expandedIds.compatRemoveIf { it !in ids }
        }
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemOccurrenceBinding.inflate(parent.layoutInflater, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), sentViewModel, expandedIds)

    class ViewHolder(private val binding: ListItemOccurrenceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            occurrencesItem: CreateOccurrence,
            occurrencesSentViewModel: OccurrencesSentViewModel,
            expandedIds: MutableSet<String>
        ) {
            val context = binding.root.context
            val spanCunt = 3;

            val imageAdapter = ImageListAdapter(context, null, false, spanCunt)
            val manager = StaggeredGridLayoutManager(spanCunt, StaggeredGridLayoutManager.VERTICAL)
            val spacing = context.resources.getDimensionPixelSize(R.dimen.spacing_normal)
            val decoration = GridSpacingItemDecoration(spanCunt, spacing, false)
            binding.imageRecyclerView.apply {
                layoutManager = manager
                adapter = imageAdapter
                addItemDecoration(decoration)
            }
            imageAdapter.submitList(occurrencesItem.occurrencePhotos)

            binding.apply {
                viewModel = occurrencesSentViewModel
                item = occurrencesItem
                executePendingBindings()
            }
            binding.expandIcon.setOnClickListener {
                val parent = this.itemView.parent as? ViewGroup ?: return@setOnClickListener
                val expanded = binding.isExpanded ?: false
                if (expanded) {
                    expandedIds.remove(occurrencesItem.occurrence.occurrenceId.toString())
                } else {
                    expandedIds.add(occurrencesItem.occurrence.occurrenceId.toString())
                }
                val transition = TransitionInflater.from(this.itemView.context)
                    .inflateTransition(R.transition.occurrence_toggle)
                TransitionManager.beginDelayedTransition(parent, transition)
                binding.executeAfter {
                    isExpanded = !expanded
                }
            }
        }
    }

    internal class DiffCallback : DiffUtil.ItemCallback<CreateOccurrence>() {
        override fun areItemsTheSame(oldItem: CreateOccurrence, newItem: CreateOccurrence) =
            oldItem.occurrence.occurrenceId == newItem.occurrence.occurrenceId

        override fun areContentsTheSame(oldItem: CreateOccurrence, newItem: CreateOccurrence) =
            oldItem == newItem
    }
}
