package com.tuan.englishforkid.presentation.practive

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.tuan.englishforkid.roomdata.Prac
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PractiveFragment : Fragment() {

    private lateinit var binding: FragmentPractiveBinding
    private val viewModelPractice: PracViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var currentPractice: Prac
    private var sharePreferences: SharedPreferences? = null
    private var pointtrue: Int = 0
    private var ttpointtrue: Int = 0
    private var ttpointfalse: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).handleShowHeader(false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_practive, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupBackPressHandler()
    }

    private fun setupObservers() {
        viewModelPractice.allPrac.observe(viewLifecycleOwner) { practices ->
            if (practices.isNotEmpty()) {
                getRandomPractice()
            }
        }
    }

    private fun getRandomPractice() {
        viewModelPractice.getRandomPrac().observe(viewLifecycleOwner) { practiceList ->
            practiceList?.firstOrNull()?.let { practice ->
                setView(practice)
            }
        }
    }

    private fun setView(practice: Prac) {
        currentPractice = practice
        with(binding) {
            Glide.with(imgItemPractive.context).load(practice.img).into(imgItemPractive)
            tvnamepractice.text = practice.vocabulary ?: ""
            tvspellingpractice.text = practice.spelling ?: ""
            tvmeanspractice.text = practice.mean ?: ""

            btna.text = practice.a?: ""
            btnb.text = practice.b?: ""
            btnc.text = practice.c?: ""
            btnd.text = practice.d?: ""

            btnNext.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(btnNext.context, R.color.xamtrang))
            llnext.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(llnext.context, R.color.xam))

            tvExit.setOnClickListener { showDialog() }
            tvhead.isSelected = true
        }

        setOnclickItem()
    }


    private fun setOnclickItem() {
        val onClick = View.OnClickListener { view ->
            checkAnswer(view as Button)
            enableNextButton()
        }
        binding.btna.setOnClickListener(onClick)
        binding.btnb.setOnClickListener(onClick)
        binding.btnc.setOnClickListener(onClick)
        binding.btnd.setOnClickListener(onClick)
    }

    private fun checkAnswer(button: Button) {
        val selectedAnswer = button.text.toString()
        val isCorrect = selectedAnswer == currentPractice.vocabulary

        if (isCorrect) {
            pointtrue++
            ttpointtrue++
            button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(button.context, R.color.True))
            playSound(R.raw.right)
        } else {
            ttpointfalse++
            button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(button.context, R.color.False))
            playSound(R.raw.wrong)
        }

        disableAllButtons()
        binding.llShowResult.visibility = View.VISIBLE
    }

    private fun enableNextButton() {
        binding.btnNext.apply {
            isClickable = true
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.btnNextIn))
            setOnClickListener {
                resetItem()
                getRandomPractice()
            }
        }
       // binding.llnext.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.llnext.context, R.color.btnNextOut))
    }

    private fun resetItem() {
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        binding.btna.backgroundTintList = ColorStateList.valueOf(whiteColor)
        binding.btnb.backgroundTintList = ColorStateList.valueOf(whiteColor)
        binding.btnc.backgroundTintList = ColorStateList.valueOf(whiteColor)
        binding.btnd.backgroundTintList = ColorStateList.valueOf(whiteColor)

        binding.llShowResult.visibility = View.GONE
        binding.btnNext.isClickable = false
        binding.btnNext.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.xamtrang))
        binding.llnext.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.xam))

        enableAllButtons()
    }

    private fun disableAllButtons() {
        binding.btna.isClickable = false
        binding.btnb.isClickable = false
        binding.btnc.isClickable = false
        binding.btnd.isClickable = false
    }

    private fun enableAllButtons() {
        binding.btna.isClickable = true
        binding.btnb.isClickable = true
        binding.btnc.isClickable = true
        binding.btnd.isClickable = true
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog()
            }
        })
    }

    private fun showDialog() {
        playSound(R.raw.pop_up)

        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val btnContinue = view.findViewById<Button>(R.id.btnContinue)
        val btnExit = view.findViewById<Button>(R.id.btnExit)

        btnContinue.setOnClickListener { dialog.dismiss() }
        btnExit.setOnClickListener {
            savePoint()
            dialog.dismiss()
            findNavController().navigate(R.id.action_PractiveFragment2_to_ResultFragment)
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun savePoint() {
        sharePreferences = activity?.getSharedPreferences("POINT", Context.MODE_PRIVATE)
        sharePreferences?.edit()?.apply {
            putString("pointtrue", pointtrue.toString())
            apply()
        }

        sharePreferences = activity?.getSharedPreferences("TOTAL", Context.MODE_PRIVATE)
        sharePreferences?.edit()?.apply {
            putString("ttrue", ttpointtrue.toString())
            putString("tfalse", ttpointfalse.toString())
            apply()
        }
    }

    private fun playSound(resourceId: Int) {
        MediaPlayer.create(context, resourceId)?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}




