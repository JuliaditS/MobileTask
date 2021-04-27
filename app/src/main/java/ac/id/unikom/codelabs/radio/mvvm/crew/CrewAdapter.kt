package ac.id.unikom.codelabs.radio.mvvm.crew

import ac.id.unikom.codelabs.radio.data.model.Crew
import ac.id.unikom.codelabs.radio.databinding.ItemCrewBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.drawable.ProgressBarDrawable

class CrewAdapter : ListAdapter<Crew, CrewAdapter.ViewHolder>(CrewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCrewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
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
        private val binding: ItemCrewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Crew) {
            binding.apply {
                crew = item
                executePendingBindings()
                crewItemThumbnail.hierarchy.setProgressBarImage(ProgressBarDrawable())
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
