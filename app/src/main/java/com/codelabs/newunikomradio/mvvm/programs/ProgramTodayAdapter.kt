package com.codelabs.newunikomradio.mvvm.programs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.newunikomradio.data.model.Program
import com.codelabs.newunikomradio.databinding.ItemProgramTodayBinding
import com.codelabs.newunikomradio.mvvm.programs.programdetail.ProgramDetailActivity
import com.codelabs.newunikomradio.utilities.INTENT_PARCELABLE
import android.app.Activity
import android.util.DisplayMetrics



class ProgramTodayAdapter : ListAdapter<Program, ProgramTodayAdapter.ViewHolder>(ProgramTodayAdapterDiffCallback()) {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemProgramTodayBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),parent.context
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
        private val binding: ItemProgramTodayBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Program) {
            binding.apply {
                program = item
                itemProgramTodayLayout.setOnClickListener {
                    val intent = Intent(context, ProgramDetailActivity::class.java)
                    intent.putExtra(INTENT_PARCELABLE, program)
                    context.startActivity(intent)
                }
                executePendingBindings()
            }
            binding.programItemThumbnail.setImageURI(item.imageUrl)
        }
    }
}

private class ProgramTodayAdapterDiffCallback : DiffUtil.ItemCallback<Program>() {

    override fun areItemsTheSame(oldItem: Program, newItem: Program): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Program, newItem: Program): Boolean {
        return oldItem == newItem
    }
}