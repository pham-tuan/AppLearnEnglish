package com.tuan.englishforkid.presentation.listen

import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentListenBinding
import com.tuan.englishforkid.presentation.practive.PracViewModel
import com.tuan.englishforkid.roomdata.Prac
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ListenFragment : Fragment(), TextToSpeech.OnInitListener {
    private lateinit var binding: FragmentListenBinding
    private val viewModelListen: PracViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var current: Prac
    private lateinit var buttonPressDownAnim: Animation
    private lateinit var buttonReleaseUpAnim: Animation
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowHeader(false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listen, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupAnimations()
        textToSpeech = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

            }
        } else {
            //  Toast.makeText(requireContext(), "Error:sound", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewModelListen.allPrac.observe(viewLifecycleOwner) { practices ->
            if (practices.isNotEmpty()) {
                getRandomListen()
            }
        }
    }

    private fun setupAnimations() {
        buttonPressDownAnim = AnimationUtils.loadAnimation(context, R.anim.button_pressed)
        buttonReleaseUpAnim = AnimationUtils.loadAnimation(context, R.anim.button_released)
    }

    private fun getRandomListen() {
        Log.d("ListenFragment", "getRandomListen called")
        viewModelListen.getRandomListen().observe(viewLifecycleOwner) { practiceList ->
            practiceList?.firstOrNull()?.let { practice ->
                setView(practice)
            }
        }
    }

    private fun setView(practice: Prac) {
        current = practice
        with(binding) {
            tvhead.isSelected = true

            result.text = practice.vocabulary
            result2.text = practice.mean

            A.text = practice.a ?: ""
            B.text = practice.b ?: ""
            C.text = practice.c ?: ""
            D.text = practice.d ?: ""

            tvcheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(tvcheck.context, R.color.xamtrang))
            llcheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(llcheck.context, R.color.xam))

            ivmic.setOnClickListener {
                playMicAnimation()
                speakWord(practice.vocabulary)
            }
            tvExit.setOnClickListener {
                findNavController().navigateUp()
            }

            setOnclickItem()
        }
    }

    private fun playMicAnimation() {
        Glide.with(this)
            .asGif()
            .load(R.drawable.mic)
            .into(binding.ivmic)

        binding.root.postDelayed({
            Glide.with(this)
                .asBitmap()
                .load(R.drawable.mic)
                .into(binding.ivmic)
        }, 1500)
    }

    private fun speakWord(word: String?) {
        word?.let {
            textToSpeech.speak(it, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    private fun setOnclickItem() {
        val onClick = View.OnClickListener { view ->
            playButtonAnimation(view)
            checkAnswer(view as Button)
            enableNextButton()
        }
        binding.A.setOnClickListener(onClick)
        binding.B.setOnClickListener(onClick)
        binding.C.setOnClickListener(onClick)
        binding.D.setOnClickListener(onClick)
    }

    private fun playButtonAnimation(view: View) {
        view.startAnimation(buttonPressDownAnim)
        view.postDelayed({
            view.startAnimation(buttonReleaseUpAnim)
        }, buttonPressDownAnim.duration)
    }


    private fun checkAnswer(tv: TextView) {
        val selectedAnswer = tv.text.toString()
        val isCorrect = selectedAnswer == current.vocabulary

        if (isCorrect) {
            binding.right.visibility = View.VISIBLE
            binding.wrong.visibility = View.GONE
            playSoundr(R.raw.right)
        } else {
            binding.right.visibility = View.GONE
            binding.wrong.visibility = View.VISIBLE
            playSoundr(R.raw.wrong)
        }

        binding.result.visibility = View.VISIBLE
        binding.result2.visibility = View.VISIBLE
        disableAll()
    }

    private fun playSoundr(resourceId: Int) {
        MediaPlayer.create(context, resourceId)?.start()
    }

    private fun enableNextButton() {
        binding.tvcheck.apply {
            isClickable = true
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.btnNextIn))
            setOnClickListener {
                resetItem()
                getRandomListen()
            }
        }
    }

    private fun resetItem() {
        binding.right.visibility = View.GONE
        binding.wrong.visibility = View.GONE
        binding.result.visibility = View.GONE
        binding.result2.visibility = View.GONE

        binding.tvcheck.isClickable = false
        binding.tvcheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.xamtrang))
        //binding.llcheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.xam))

        binding.A.isClickable = true
        binding.B.isClickable = true
        binding.C.isClickable = true
        binding.D.isClickable = true
    }
    private fun disableAll() {
        binding.A.isClickable = false
        binding.B.isClickable = false
        binding.C.isClickable = false
        binding.D.isClickable = false
    }



    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}