package com.tuan.englishforkid.presentation.favorite

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.ItemFacvoriteBinding
import com.tuan.englishforkid.presentation.detail1.TextToSpeechListener
import com.tuan.englishforkid.roomdata.AppDatabase
import com.tuan.englishforkid.roomdata.Favor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavorAdapter(private var items: List<Favor>, private val ttsListener: TextToSpeechListener) : RecyclerView.Adapter<FavorAdapter.FavorViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var frontAnim: AnimatorSet
    private lateinit var backAnim: AnimatorSet
    private var isFront = true

    class FavorViewHolder(val binding: ItemFacvoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavorViewHolder {
        val binding = ItemFacvoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavorViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FavorViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvName.text = item.vocabulary
            tvSpelling.text = item.spelling
            tvMeans.text = item.mean
            like.isChecked = item.note == "1"
            Glide.with(holder.itemView.context).load(item.img).into(ivImgItem)

            imgsound.setOnClickListener {
                ttsListener.speakOutVocabulary(item.vocabulary ?: "")
            }

            like.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    CoroutineScope(Dispatchers.IO).launch {
                        AppDatabase.getDatabase(root.context).dao().deleteByVoca(item.vocabulary.toString())
                        // Sau khi xóa xong, cập nhật UI trên main thread
                        launch(Dispatchers.Main) {
                            removeItem(position)
                            Toast.makeText(root.context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            root.setOnClickListener { view ->
                val scale = view.resources.displayMetrics.density
                cardFront.cameraDistance = 8000 * scale
                cardBack.cameraDistance = 8000 * scale

                frontAnim = AnimatorInflater.loadAnimator(view.context, R.anim.flip_front_anim) as AnimatorSet
                backAnim = AnimatorInflater.loadAnimator(view.context, R.anim.flip_back_anim) as AnimatorSet

                cardFront.setOnClickListener {
                    if (isFront) {
                        frontAnim.setTarget(cardFront)
                        backAnim.setTarget(cardBack)
                        frontAnim.start()
                        backAnim.start()
                        isFront = false
                    } else {
                        frontAnim.setTarget(cardBack)
                        backAnim.setTarget(cardFront)
                        backAnim.start()
                        frontAnim.start()
                        isFront = true
                    }
                }
            }
        }
    }

    private fun playSound(soundUrl: String?) {
        if (soundUrl.isNullOrEmpty()) return

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(soundUrl)
            setOnPreparedListener { start() }
            setOnCompletionListener {
                release()
                mediaPlayer = null
            }
            prepareAsync()
        }
    }

    fun updateData(newItems: List<Favor>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position in items.indices) {
            val mutableItems = items.toMutableList()
            mutableItems.removeAt(position)
            items = mutableItems
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }
}
