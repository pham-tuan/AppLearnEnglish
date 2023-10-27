package com.tuan.englishforkid.presentation.practive

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentPractiveBinding
import com.tuan.englishforkid.ext.hideLoading
import com.tuan.englishforkid.ext.showLoading
import com.tuan.englishforkid.model.Practice
import com.tuan.englishforkid.model.PracticeResponse
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


@AndroidEntryPoint
class PractiveFragment : Fragment() {

    private lateinit var binding: FragmentPractiveBinding
    private val viewModelPractice: PracticeViewModel by viewModels()
    private val listPractice: ArrayList<Practice> = ArrayList()
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var currentPractice: Practice // Thêm biến để lưu trữ dữ liệu hiện tại
    private var sharePreferences: SharedPreferences? = null
    private var pointtrue: Int? = 0
    private var ttpointtrue: Int? = 0
    private var ttpointfalse: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).handleShowHeader(false)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_practive,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.practiceviewModel = viewModelPractice
        setUpObserver()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView(Practice())
    }

    private fun setView(practice: Practice) {
        currentPractice = practice // Lưu trữ dữ liệu hiện tại
        // Cập nhật giao diện với dữ liệu của practice
        Glide.with(binding.imgItemPractive.context).load(practice.imgpractice)
            .into(binding.imgItemPractive)
        binding.tvnamepractice.text = practice.vocabulary ?: ""
        binding.tvspellingpractice.text = practice.spelling ?: ""
        binding.tvmeanspractice.text = practice.means ?: ""
        binding.btna.text = practice.a ?: ""
        binding.btnb.text = practice.b ?: ""
        binding.btnc.text = practice.c ?: ""
        binding.btnd.text = practice.d ?: ""

        binding.imgsound.setOnClickListener {
            val uri = Uri.parse(practice.sound)
            if (uri != null) {
                if (mediaPlayer != null) {
                    mediaPlayer?.release()
                }
                mediaPlayer = MediaPlayer.create(binding.root.context, uri)
                mediaPlayer?.start()
            }
        }

        binding.btnNext.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(binding.btnNext.context, R.color.xamtrang))
        binding.llnext.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(binding.llnext.context, R.color.xam))

        binding.tvExit.setOnClickListener {
            showDialog()
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Xử lý khi nút back được nhấn
                showDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setOnclickItem()
    }

    private fun setOnclickItem() {
        val onClick = View.OnClickListener { view ->
            checkAnswer(view, true) // Hàm để kiểm tra đáp án
            binding.btnNext.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(binding.btnNext.context, R.color.btnNextIn))
            binding.llnext.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(binding.llnext.context, R.color.btnNextOut))
        }
        binding.btna.setOnClickListener(onClick)
        binding.btnb.setOnClickListener(onClick)
        binding.btnc.setOnClickListener(onClick)
        binding.btnd.setOnClickListener(onClick)

    }

    private fun checkAnswer(view: View, check: Boolean) {
        val selectedAnswer = (view as Button).text.toString()
        val isCorrect = currentPractice.result

        if (isCorrect == selectedAnswer) {
            //nếu đúng
            pointtrue = pointtrue!! + 1    //tăng điểm lên
            ttpointtrue = ttpointtrue!! + 1

            view.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(view.context, R.color.True)
            )
            val mediaPlayerT = MediaPlayer.create(context, R.raw.right)
            mediaPlayerT.start()
        } else if(isCorrect != selectedAnswer) {
            // sai
            ttpointfalse = ttpointfalse!! + 1
            view.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(view.context, R.color.False)
            )
            val mediaPlayerF = MediaPlayer.create(context, R.raw.wrong)
            mediaPlayerF.start()
        }else{

        }
        binding.btna.isClickable = false
        binding.btnb.isClickable = false
        binding.btnc.isClickable = false
        binding.btnd.isClickable = false
        binding.llShowResult.visibility = View.VISIBLE

        if (check) {
            binding.btnNext.isClickable = true
            binding.btnNext.setOnClickListener {
                ressetItem()   //reset cho cc item trở về ban đầu
                val randomPractice = getRandom()
                if (randomPractice != null) {
                    setView(randomPractice)
                }
            }
        }

    }

    private fun ressetItem() {
        val context = binding.root.context // Lấy tham chiếu đến context một lần để sử dụng lại
        val whiteColor = ContextCompat.getColor(context, R.color.white)
        val xamTrangColor = ContextCompat.getColor(context, R.color.xamtrang)
        val xamColor = ContextCompat.getColor(context, R.color.xam)

        binding.btna.backgroundTintList = ColorStateList.valueOf(whiteColor)
        binding.btnb.backgroundTintList = ColorStateList.valueOf(whiteColor)
        binding.btnc.backgroundTintList = ColorStateList.valueOf(whiteColor)
        binding.btnd.backgroundTintList = ColorStateList.valueOf(whiteColor)

        binding.llShowResult.visibility = View.GONE
        binding.btnNext.isClickable = false
        binding.btnNext.backgroundTintList = ColorStateList.valueOf(xamTrangColor)
        binding.llnext.backgroundTintList = ColorStateList.valueOf(xamColor)
    }

    private fun updatePracticeList(practices: List<Practice>) {
        listPractice.clear()
        listPractice.addAll(practices)
        val randomPractice = getRandom()
        if (randomPractice != null) {
            setView(randomPractice)
        }
    }

    private fun setUpObserver() {
        viewModelPractice.getlistPractice("1").observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    hideLoading()
                    val value = data.data?.body() as PracticeResponse
                    val practices = value.listPractice ?: emptyList()
                    updatePracticeList(practices)
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

    private fun getRandom(): Practice? {
        return if (listPractice.isNotEmpty()) {
            val randomIndex = Random.nextInt(listPractice.size)
            listPractice[randomIndex]
        } else {
            null
        }
    }


    private fun showDialog() {
        val mediaPlayer = MediaPlayer.create(context,R.raw.pop_up)
        mediaPlayer.start()

        val dialog = context?.let { BottomSheetDialog(it) }
        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val btnContinue = view.findViewById<Button>(com.tuan.englishforkid.R.id.btnContinue)
        val btnExit = view.findViewById<Button>(com.tuan.englishforkid.R.id.btnExit)

        btnContinue.setOnClickListener {
            dialog?.dismiss()
        }
        btnExit.setOnClickListener {
            savePoint()
            dialog?.dismiss()
            findNavController().navigate(R.id.action_PractiveFragment_to_ResultFragment)
        }
        dialog?.setCancelable(false)
        dialog?.setContentView(view)
        dialog?.show()
    }

    private fun savePoint() {
        val valueTrue = pointtrue.toString()

        sharePreferences = activity?.getSharedPreferences("POINT", Context.MODE_PRIVATE) //?: return
        with(sharePreferences?.edit()) {
            this?.putString("pointtrue", valueTrue)
            this?.apply()
        }

        val ttFlase = ttpointfalse.toString()
        val ttTrue = ttpointtrue.toString()
        sharePreferences = activity?.getSharedPreferences("TOTAL", Context.MODE_PRIVATE) //?: return
        with(sharePreferences?.edit()) {
            this?.putString("ttrue", ttTrue)
            this?.putString("tfalse", ttFlase)
            this?.apply()
        }
        Toast.makeText(requireContext(), "true: ${ttTrue}", Toast.LENGTH_SHORT).show()
        Toast.makeText(requireContext(), "flase: ${ttFlase}", Toast.LENGTH_SHORT).show()

    }

}




