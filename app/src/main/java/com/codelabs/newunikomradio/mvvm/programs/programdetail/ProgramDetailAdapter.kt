package com.codelabs.newunikomradio.mvvm.programs.programdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.newunikomradio.data.model.Crew
import com.codelabs.newunikomradio.databinding.ItemAnnouncerBinding

class ProgramDetailAdapter : ListAdapter<Crew, ProgramDetailAdapter.ViewHolder>(AnnouncerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAnnouncerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val crews = getItem(position)
        holder.apply {
            bind(crews)
            itemView.tag = crews
        }
    }

    class ViewHolder(
        private val binding: ItemAnnouncerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Crew) {
            binding.apply {
                crew = item
                executePendingBindings()
            }

        }
    }
}


private class AnnouncerDiffCallback : DiffUtil.ItemCallback<Crew>() {

    override fun areItemsTheSame(oldItem: Crew, newItem: Crew): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Crew, newItem: Crew): Boolean {
        return oldItem == newItem
    }
}
