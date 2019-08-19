package com.codelabs.unikomradio.mvvm.programs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.databinding.ItemProgramTodayBinding
import com.codelabs.unikomradio.mvvm.programs.programdetail.ProgramDetailActivity
import com.codelabs.unikomradio.utilities.INTENT_PARCELABLE
import timber.log.Timber

class ProgramTodayAdapter : ListAdapter<Program, ProgramTodayAdapter.ViewHolder>(ProgramTodayAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProgramTodayBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),parent.context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val topChart = getItem(position)
        holder.apply {
            bind(createOnClickListener(), topChart)
            itemView.tag = topChart
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
                Timber.d("Ada datanya padahal: ${program!!.title}")
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