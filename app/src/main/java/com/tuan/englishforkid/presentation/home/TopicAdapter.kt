package com.tuan.englishforkid.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuan.englishforkid.databinding.ItemTopicBinding
import com.tuan.englishforkid.model.Topic

class TopicAdapter : ListAdapter<Topic, TopicAdapter.TopicViewHolder>(DiffCallback()) {
    var onItemClick: ((Topic) -> Unit)? = null

    inner class TopicViewHolder(val binding: ItemTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Topic) {
            binding.tvTopicName.text = data.title?:""
            Glide.with(binding.ivTopic.context).load(data.imgtopic).into(binding.ivTopic)
            binding.root.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
     //   holder.bind(getItem(position))
        if(holder is TopicViewHolder){
            Log.d("aaa","bind($position)")
            holder.bind(getItem(position))
        }
    }
}


class DiffCallback : DiffUtil.ItemCallback<Topic>() {

    override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
        return oldItem.idTopic == newItem.idTopic
    }

    override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
        return oldItem == newItem
    }
}