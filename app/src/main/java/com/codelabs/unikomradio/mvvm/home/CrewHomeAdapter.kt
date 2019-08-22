package com.codelabs.unikomradio.mvvm.home

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.unikomradio.data.model.Crew
import com.codelabs.unikomradio.databinding.ItemHomeCrewBinding

class CrewHomeAdapter : ListAdapter<Crew, CrewHomeAdapter.ViewHolder>(CrewDiffCallback()) {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
                ItemHomeCrewBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val displaymetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displaymetrics)

        val devicewidth = (displaymetrics.widthPixels * 0.42).toInt()

        val topChart = getItem(position)
        holder.apply {
            bind(createOnClickListener(), topChart)
            itemView.tag = topChart
            itemView.layoutParams.width = devicewidth
        }
    }

    private fun createOnClickListener(): View.OnClickListener {
        return View.OnClickListener {}
    }

    class ViewHolder(
            private val binding: ItemHomeCrewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Crew) {
            binding.apply {
                crew = item
                executePendingBindings()
                crewItemThumbnail.setImageURI(item.userPhoto)
            }
        }
    }
}

private class CrewDiffCallback : DiffUtil.ItemCallback<Crew>() {

    override fun areItemsTheSame(oldItem: Crew, newItem: Crew): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Crew, newItem: Crew): Boolean {
        return oldItem == newItem
    }
}
