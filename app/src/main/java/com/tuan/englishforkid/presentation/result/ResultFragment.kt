package com.tuan.englishforkid.presentation.result

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private var sharePreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowHeader(false)
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handle()
    }

    private fun handle() {
        sharePreferences = activity?.getSharedPreferences("TOTAL", Context.MODE_PRIVATE)
        val pointtrue = sharePreferences?.getString("ttrue", "0")?.toInt()
        val pointfalse = sharePreferences?.getString("tfalse", "0")?.toInt()

        val totalScore = pointtrue?.plus(pointfalse ?: 0)

        val percen: Float? = pointtrue?.toFloat()?.div(totalScore?.toFloat() ?: 1.0f)?.times(100)

        binding.txtper.text = String.format("%.2f%%", percen ?: 0.0f)
        binding.Prog.progress = percen?.toInt()!!
        binding.showResult.text = "Bạn đã trả lời đúng $pointtrue/$totalScore câu"

        binding.btnPracice.setOnClickListener {
            findNavController().navigate(R.id.action_ResultFragment_to_PracticeFragment)
            reserPoint()
        }
        binding.btnExit.setOnClickListener {
            findNavController().navigate(R.id.action_ResultFragment_to_HomeFragment)
            reserPoint()
        }
    }

    private fun reserPoint() {
        val ttTrue = 0
        val ttFalse = 0
        val pointTrue = ttTrue.toString()
        val pointFalse = ttFalse.toString()
        sharePreferences = activity?.getSharedPreferences("TOTAL", Context.MODE_PRIVATE) //?: return
        with(sharePreferences?.edit()) {
            this?.putString("ttrue", pointTrue)
            this?.putString("tfalse", pointFalse)
            this?.apply()
        }
    }


}
