// DetailFragment.kt
package com.tuan.englishforkid.presentation.detail1

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.ColumnInfo
import androidx.viewpager2.widget.ViewPager2
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentDetailBinding
import com.tuan.englishforkid.ext.hideLoading
import com.tuan.englishforkid.ext.showLoading
import com.tuan.englishforkid.model.Detail
import com.tuan.englishforkid.model.DetailResponse
import com.tuan.englishforkid.model.Topic
import com.tuan.englishforkid.roomdata.AppDatabase
import com.tuan.englishforkid.roomdata.Prac
import com.tuan.englishforkid.utils.Constant
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@AndroidEntryPoint
class DetailFragment : Fragment(), TextToSpeech.OnInitListener, TextToSpeechListener {
    private lateinit var binding: FragmentDetailBinding
    private val viewModelDetail: DetailViewModel by viewModels()
    private lateinit var detailAdapter: DetailAdapter
    private val listDetail: ArrayList<Detail> = ArrayList()
    private lateinit var ttsMean: TextToSpeech
    private lateinit var ttsVocabulary: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).handleShowHeader(false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.viewPagerDetail.isUserInputEnabled = false
        binding.lifecycleOwner = this
        binding.detailviewModel = viewModelDetail
        setupViewPager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Topic>(Constant.KEY_DATA)?.let {
            setUpObserver(it)
        }

        ttsMean = TextToSpeech(context, this)
        ttsVocabulary = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = ttsVocabulary.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTSUS", "English language not supported")
                }
            } else {
                Log.e("TTSUS", "Initialization of English TTS failed")
            }
        })
    }

    override fun onDestroy() {
        if (::ttsMean.isInitialized) {
            ttsMean.stop()
            ttsMean.shutdown()
        }
        if (::ttsVocabulary.isInitialized) {
            ttsVocabulary.stop()
            ttsVocabulary.shutdown()
        }
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = ttsMean.setLanguage(Locale("vi", "VN"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Vietnamese language not supported")
            }
        } else {
            Log.e("TTS", "Initialization of Vietnamese TTS failed")
        }
    }

    private fun setupViewPager() {
        detailAdapter = DetailAdapter(this)
        binding.viewPagerDetail.adapter = detailAdapter

        binding.viewPagerDetail.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentItem = detailAdapter.currentList[position]
                speakOutMean(currentItem.means)
                viewLifecycleOwner.lifecycleScope.launch {
                    savePracToDatabase(currentItem)
                }
            }
        })

        binding.tvExit.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvNext.setOnClickListener {
            val currentItemIndex = binding.viewPagerDetail.currentItem
            if (currentItemIndex + 1 < detailAdapter.itemCount) {
                try {
                    val currentItem = detailAdapter.currentList[currentItemIndex]
                    Log.d("DetailFragment", "Current item: ${currentItem.vocabulary}")

                    // Lưu từ vựng hiện tại vào Room database
                    viewLifecycleOwner.lifecycleScope.launch {
                        savePracToDatabase(currentItem)
                    }
                    // Chuyển sang item tiếp theo
                    binding.viewPagerDetail.currentItem = currentItemIndex + 1
                    Log.d("DetailFragment", "Moved to next item: ${currentItemIndex + 1}")
                } catch (e: Exception) {
                    Log.e("DetailFragment", "Error when moving to next item", e)
                    Toast.makeText(context, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(activity, "HẾT GÒI", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun speakOutVocabulary(text: String) {
        if (::ttsVocabulary.isInitialized) {
            ttsVocabulary.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            Log.e("Detailvocabulary", "ttsVocabulary not initialized")
        }
    }

    private suspend fun savePracToDatabase(detail: Detail) {
        withContext(Dispatchers.IO) {
            try {
                val prac = Prac(
                    id = null,
                    type = detail.type,
                    img = detail.imgdetail,
                    vocabulary = detail.vocabulary,
                    spelling = detail.spelling,
                    mean = detail.means,
                    sound = detail.sound,
                    status = detail.status,
                    note = detail.note,
                    result = detail.result,
                    a = detail.a,
                    b = detail.b,
                    c = detail.c,
                    d = detail.d,
                )
                // Kiểm tra xem từ vựng đã tồn tại chưa
                val exists = AppDatabase.getDatabase(requireContext()).dao()
                    .exists(detail.vocabulary.toString())
                if (exists == 0) {
                    // Nếu từ vựng chưa tồn tại, thêm vào database
                    val result = AppDatabase.getDatabase(requireContext()).dao().insert(prac)
                    Log.d("DetailFragment", "Insert practice result: $result")
                } else {

                }
            } catch (e: Exception) {
                Log.e("DetailFragment", "Error saving practice", e)
            }
        }
    }

    private fun setUpObserver(topic: Topic) {
        binding.title.text = topic.title
        viewModelDetail.getListDetail(topic.type ?: "").observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    hideLoading()
                    val value = data.data?.body() as DetailResponse
                    value.listDetail?.forEach {
                        listDetail.add(it)
                        Log.d("listDetail", listDetail.toString())
                    }
                    detailAdapter.submitList(listDetail)
                }

                DataResult.Status.LOADING -> {
                    showLoading()
                }

                DataResult.Status.ERROR -> {
                    hideLoading()
                    Toast.makeText(context, "ERROR_API", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun speakOutMean(text: String?) {
        if (::ttsMean.isInitialized) {
            text?.let {
                ttsMean.speak(it, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }
    }
}
