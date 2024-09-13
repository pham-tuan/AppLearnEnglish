package com.tuan.englishforkid.presentation.favorite

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentFavoriteBinding
import com.tuan.englishforkid.presentation.detail1.TextToSpeechListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class FavoriteFragment : Fragment(), TextToSpeechListener {
    lateinit var binding: FragmentFavoriteBinding
    private lateinit var ttsVocabulary: TextToSpeech
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var favorPagerAdapter: FavorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowHeader(false)
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ttsVocabulary = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = ttsVocabulary.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "English language not supported")
                }
            } else {
                Log.e("TTS", "Initialization of English TTS failed")
            }
        }

        favorPagerAdapter = FavorAdapter(listOf(), this)
        binding.viewPagerFavor.adapter = favorPagerAdapter

        viewModel.getRandomFavorites().observe(viewLifecycleOwner, Observer { favorites ->
            favorPagerAdapter.updateData(favorites)
            if (favorites.isEmpty()) {
                showNoDataDialog()
            } else {
                dismissNoDataDialog()
            }
        })

        setupView()
    }

    private fun setupView() {
        binding.tvExit.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.viewPagerFavor.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        binding.tvNext.setOnClickListener {
            val currentItem = binding.viewPagerFavor.currentItem
            val itemCount = favorPagerAdapter.itemCount
            if (currentItem < itemCount - 1) {
                binding.viewPagerFavor.currentItem = currentItem + 1
            }
        }
    }

    private fun showNoDataDialog() {
        val dialog = FavorDialog()
        dialog.show(childFragmentManager, "NoDataDialog")
    }

    private fun dismissNoDataDialog() {
        val dialog = childFragmentManager.findFragmentByTag("NoDataDialog") as DialogFragment?
        dialog?.dismiss()
    }

    override fun speakOutVocabulary(text: String) {
        if (::ttsVocabulary.isInitialized) {
            ttsVocabulary.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            Log.e("Detailvocabulary", "ttsVocabulary not initialized")
        }
    }

    override fun onDestroy() {
        if (::ttsVocabulary.isInitialized) {
            ttsVocabulary.stop()
            ttsVocabulary.shutdown()
        }
        super.onDestroy()
    }
}
