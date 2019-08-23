package com.codelabs.unikomradio.mvvm.home

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.unikomradio.data.model.Banner
import com.codelabs.unikomradio.databinding.ItemBannerBinding
import timber.log.Timber

class BannerAdapter : ListAdapter<Banner, BannerAdapter.ViewHolder>(BannerDiffCallback()) {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       context = parent.context
        return ViewHolder(
            ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val news = getItem(position)
        val displaymetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displaymetrics)
        //if you need three fix imageview in width
        val devicewidth = (displaymetrics.widthPixels * 0.9).toInt()

        holder.apply {
            bind(news)
            itemView.tag = news
            itemView.layoutParams.width = devicewidth
        }
    }

    class ViewHolder(
        private val binding: ItemBannerBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Banner) {

            binding.apply {
                banner = item
                executePendingBindings()
                binding.programBannerThumbnail.setImageURI(item.imageUrl)
            }

        }
    }
}


private class BannerDiffCallback : DiffUtil.ItemCallback<Banner>() {

    override fun areItemsTheSame(oldItem: Banner, newItem: Banner): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Banner, newItem: Banner): Boolean {
        return oldItem == newItem
    }
}