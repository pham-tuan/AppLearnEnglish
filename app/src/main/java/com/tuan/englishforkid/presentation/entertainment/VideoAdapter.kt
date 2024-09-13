package com.tuan.englishforkid.presentation.entertainment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tuan.englishforkid.databinding.ItemVideoBinding
import com.tuan.englishforkid.model.Detail

class VideoAdapter(private val list: List<Video>) :
RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    var onClickItem: ((Video) -> Unit)? = null

    inner class VideoViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.imgFilm.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.imgFilm.context,
                    video.image
                )
            )
            binding.tvFilmName.text = video.filmName

            binding.root.setOnClickListener {
                onClickItem?.invoke(video)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(list[position])
    }
    
}
