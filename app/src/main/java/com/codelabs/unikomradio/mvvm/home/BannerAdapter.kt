package com.codelabs.unikomradio.mvvm.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.unikomradio.data.model.Banner
import com.codelabs.unikomradio.databinding.ItemBannerBinding
import timber.log.Timber

class BannerAdapter : ListAdapter<Banner, BannerAdapter.ViewHolder>(BannerDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val news = getItem(position)
        holder.apply {
            bind(news)
            itemView.tag = news
        }
    }

    class ViewHolder(
        private val binding: ItemBannerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Banner) {

            binding.apply {
                banner = item
                executePendingBindings()
                Timber.i("data: $banner")
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