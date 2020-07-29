package com.codelabs.newunikomradio.mvvm.streaming.streaming_topcharts

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.newunikomradio.R
import com.codelabs.newunikomradio.data.model.TopChart
import com.codelabs.newunikomradio.databinding.ItemTopchartsBinding
import com.codelabs.newunikomradio.utilities.helper.Preferences

class StreamingTopchartsAdapter :
    ListAdapter<TopChart, StreamingTopchartsAdapter.ViewHolder>(StreamingDiffCallback()) {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemTopchartsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topChart = getItem(position)
        holder.apply {
            bind(topChart)
            setImageState(stateRank(topChart.currentRank, topChart.rankBefore))
            itemView.tag = topChart
            if (!Preferences(context).isLightMode()) {
                itemView.background =
                    ColorDrawable(context.resources.getColor(R.color.colorPrimary))
            }
        }
    }

    private fun stateRank(currentRank: Int, rankBefore: Int): Int {
        var state = IDLE_STATE
        if (currentRank > rankBefore) {
            state = DOWN_STATE
        } else if (currentRank < rankBefore) {
            state = UP_STATE
        }
        return state
    }

    private fun createOnClickListener(): View.OnClickListener {
        return View.OnClickListener {}
    }

    class ViewHolder(
        private val binding: ItemTopchartsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TopChart) {
            binding.apply {
                topchart = item
                executePendingBindings()
            }
        }

        fun setImageState(state: Int) {
            val image = binding.itemTopchartPositionChange
            when (state) {
                DOWN_STATE -> image.setImageResource(R.drawable.icon_down_white)
                UP_STATE -> image.setImageResource(R.drawable.icon_up_white)
                else -> image.setImageResource(R.drawable.icon_middle)
            }
        }

    }

    companion object {
        const val DOWN_STATE = 0
        const val UP_STATE = 1
        const val IDLE_STATE = 2
    }
}

private class StreamingDiffCallback : DiffUtil.ItemCallback<TopChart>() {

    override fun areItemsTheSame(oldItem: TopChart, newItem: TopChart): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: TopChart, newItem: TopChart): Boolean {
        return oldItem == newItem
    }
}