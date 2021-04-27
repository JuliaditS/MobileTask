package ac.id.unikom.codelabs.radio.mvvm.programs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ac.id.unikom.codelabs.radio.data.model.Program
import ac.id.unikom.codelabs.radio.databinding.ItemProgramBinding
import ac.id.unikom.codelabs.radio.mvvm.programs.programdetail.ProgramDetailActivity
import ac.id.unikom.codelabs.radio.utilities.INTENT_PARCELABLE
import com.facebook.drawee.drawable.ProgressBarDrawable

class ProgramAdapter : ListAdapter<Program, ProgramAdapter.ViewHolder>(ProgramDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemProgramBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),parent.context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val topChart = getItem(position)
        holder.apply {
            bind(topChart)
            itemView.tag = topChart
        }
    }

    class ViewHolder(
        private val binding: ItemProgramBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Program) {

            binding.apply {
                program = item
                executePendingBindings()
                programItemLayout.setOnClickListener {
                    val intent = Intent(context,ProgramDetailActivity::class.java)
                    intent.putExtra(INTENT_PARCELABLE, program)
                    context.startActivity(intent)
                }
                programItemThumbnail.hierarchy.setProgressBarImage(
                    ProgressBarDrawable()
                )
                programItemThumbnail.setImageURI(item.imageUrl)
            }

        }
    }
}


private class ProgramDiffCallback : DiffUtil.ItemCallback<Program>() {

    override fun areItemsTheSame(oldItem: Program, newItem: Program): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Program, newItem: Program): Boolean {
        return oldItem == newItem
    }
}
