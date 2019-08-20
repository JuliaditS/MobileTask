package com.codelabs.unikomradio.mvvm.news

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.unikomradio.data.model.News
import com.codelabs.unikomradio.databinding.ItemNewsBinding
import com.codelabs.unikomradio.mvvm.news.newsdetail.NewsDetailActivity
import com.codelabs.unikomradio.utilities.INTENT_PARCELABLE
import timber.log.Timber

class NewsAdapter : ListAdapter<News, NewsAdapter.ViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), parent.context
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
        private val binding: ItemNewsBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: News) {

            binding.apply {
                news = item
                executePendingBindings()
                Timber.i("data: $news")
                newsItemLayout.setOnClickListener {
                    val intent = Intent(context, NewsDetailActivity::class.java)
                    intent.putExtra(INTENT_PARCELABLE, news)
                    context.startActivity(intent)
                }
                binding.newsItemThumbnail.setImageURI(item.imageUrl)
            }

        }
    }
}


private class NewsDiffCallback : DiffUtil.ItemCallback<News>() {

    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }
}
