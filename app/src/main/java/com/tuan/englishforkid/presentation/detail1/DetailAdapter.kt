package com.tuan.englishforkid.presentation.detail1

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.ItemDetailBinding
import com.tuan.englishforkid.model.Detail
import com.tuan.englishforkid.model.Topic

class DetailAdapter : ListAdapter<Detail, DetailAdapter.DetailViewHolder>(ITEM_COMPARATOR) {

    lateinit var front_anim: AnimatorSet
    lateinit var back_anim: AnimatorSet
    var isFront = true
    private var mediaPlayer: MediaPlayer? = null

    inner class DetailViewHolder(val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataDetail: Detail) {
            Glide.with(binding.ivImgItem.context).load(dataDetail.imgdetail).into(binding.ivImgItem)
            binding.tvName.text = dataDetail.vocabulary ?: ""
            binding.tvSpelling.text = dataDetail.spelling ?: ""
            binding.tvMeans.text = dataDetail.means ?: ""
            binding.imgsound.setOnClickListener {
                   // val dataDetail = getItem(bindingAdapterPosition)
                    val uri = Uri.parse(dataDetail.sound)
                    // Kiểm tra xem âm thanh có sẵn hay không
                    if (uri != null) {
                        if (mediaPlayer != null) {
                            mediaPlayer?.release()
                        }
                        mediaPlayer = MediaPlayer.create(binding.root.context, uri)
                        mediaPlayer?.start()
                    }
                }

            binding.root.setOnClickListener { view ->
                val scale : Float? = view?.resources?.displayMetrics?.density
                binding.cardFront.cameraDistance = 8000 * scale!!
                binding.cardBack.cameraDistance = 8000 * scale!!

                front_anim = AnimatorInflater.loadAnimator(view.context, R.anim.flip_front_anim) as AnimatorSet
                back_anim  = AnimatorInflater.loadAnimator(view.context, R.anim.flip_back_anim) as AnimatorSet

                binding.cardFront.setOnClickListener {
                    if(isFront)
                    {
                        front_anim.setTarget(binding.cardFront);
                        back_anim.setTarget(binding.cardBack);
                        front_anim.start()
                        back_anim.start()
                        isFront = false

                    }else
                    {
                        front_anim.setTarget(binding.cardBack)
                        back_anim.setTarget(binding.cardFront)
                        back_anim.start()
                        front_anim.start()
                        isFront =true
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(ItemDetailBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Detail>() {
            override fun areItemsTheSame(oldItem: Detail, newItem: Detail): Boolean {
                return oldItem.iddata == newItem.iddata
            }
            override fun areContentsTheSame(oldItem: Detail, newItem: Detail): Boolean {
                return oldItem == newItem
            }

        }
    }
}
