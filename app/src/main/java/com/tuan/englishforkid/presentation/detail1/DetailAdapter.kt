package com.tuan.englishforkid.presentation.detail1

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.ItemDetailBinding
import com.tuan.englishforkid.model.Detail
import com.tuan.englishforkid.roomdata.AppDatabase
import com.tuan.englishforkid.roomdata.Favor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DetailAdapter @Inject constructor(private val ttsListener: TextToSpeechListener)  : ListAdapter<Detail, DetailAdapter.DetailViewHolder>(ITEM_COMPARATOR) {

    private lateinit var frontAnim: AnimatorSet
    private lateinit var backAnim: AnimatorSet
    private var isFront = true

    inner class DetailViewHolder(private val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataDetail: Detail) {
            with(binding) {
                Glide.with(ivImgItem.context).load(dataDetail.imgdetail).into(ivImgItem)
                tvName.text = dataDetail.vocabulary ?: ""
                tvSpelling.text = dataDetail.spelling ?: ""
                tvMeans.text = dataDetail.means ?: ""

                imgsound.setOnClickListener {
                    ttsListener.speakOutVocabulary(dataDetail.vocabulary ?: "")
                }

                like.isChecked = dataDetail.note == "1"

                like.setOnCheckedChangeListener { _, isChecked ->
                    handleFavoriteChange(dataDetail, isChecked)
                }

                setupCardFlip()
            }
        }


        private fun handleFavoriteChange(dataDetail: Detail, isChecked: Boolean) {
            val favor = Favor(
                id = null,
                img = dataDetail.imgdetail ?: "",
                vocabulary = dataDetail.vocabulary ?: "",
                spelling = dataDetail.spelling ?: "",
                mean = dataDetail.means ?: "",
                sound = dataDetail.sound ?: "",
                status = dataDetail.status ?: "",
                note = if (isChecked) "1" else "0"
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(binding.root.context)
                    if (isChecked) {
                        val existsFavor = db.dao().existsFavor(favor.vocabulary.toString())
                        if (existsFavor == 0) {
                            db.dao().insertfavor(favor)
                            withContext(Dispatchers.Main) {
                                showSnackbar("Đã thêm vào yêu thích")
                            }
                        }
                    } else {
                        db.dao().deleteByVoca(favor.vocabulary.toString())
                        withContext(Dispatchers.Main) {
                            showSnackbar("Đã xóa khỏi yêu thích")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("DetailAdapter", "Error handling favorite change", e)
                    withContext(Dispatchers.Main) {
                        showSnackbar("Đã xảy ra lỗi: ${e.message}")
                    }
                }
            }
        }

        private fun showSnackbar(message: String) {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }


        private fun setupCardFlip() {
            val scale = binding.root.resources.displayMetrics.density
            binding.cardFront.cameraDistance = 8000 * scale
            binding.cardBack.cameraDistance = 8000 * scale

            frontAnim = AnimatorInflater.loadAnimator(
                binding.root.context,
                R.anim.flip_front_anim
            ) as AnimatorSet
            backAnim = AnimatorInflater.loadAnimator(
                binding.root.context,
                R.anim.flip_back_anim
            ) as AnimatorSet

            binding.cardFront.setOnClickListener {
                flipCard()
            }
        }

        private fun flipCard() {
            if (isFront) {
                frontAnim.setTarget(binding.cardFront)
                backAnim.setTarget(binding.cardBack)
                frontAnim.start()
                backAnim.start()
            } else {
                frontAnim.setTarget(binding.cardBack)
                backAnim.setTarget(binding.cardFront)
                backAnim.start()
                frontAnim.start()
            }
            isFront = !isFront
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailViewHolder(binding)
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