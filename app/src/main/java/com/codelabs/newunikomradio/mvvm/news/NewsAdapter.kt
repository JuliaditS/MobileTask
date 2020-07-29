package com.codelabs.newunikomradio.mvvm.news

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.newunikomradio.data.model.News
import com.codelabs.newunikomradio.databinding.ItemNewsBinding
import com.codelabs.newunikomradio.mvvm.news.newsdetail.NewsDetailActivity
import com.codelabs.newunikomradio.utilities.INTENT_PARCELABLE
import com.facebook.drawee.drawable.ProgressBarDrawable

class NewsAdapter : ListAdapter<News, NewsAdapter.ViewHolder>(NewsDiffCallback()) {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val displaymetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displaymetrics)

        val devicewidth = (displaymetrics.widthPixels * 0.90).toInt()
        val news = getItem(position)
        holder.apply {
            bind(news)
            itemView.tag = news
            itemView.layoutParams.width = devicewidth
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
                newsItemLayout.setOnClickListener {
                    val intent = Intent(context, NewsDetailActivity::class.java)
                    intent.putExtra(INTENT_PARCELABLE, news)
                    context.startActivity(intent)
                }
                newsItemThumbnail.hierarchy.setProgressBarImage(
                    ProgressBarDrawable()
                )

                newsItemThumbnail.setImageURI(item.imageUrl)
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
