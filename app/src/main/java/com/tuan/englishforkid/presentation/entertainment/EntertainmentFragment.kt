package com.tuan.englishforkid.presentation.entertainment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentEntertainmentBinding
import com.tuan.englishforkid.model.Topic
import com.tuan.englishforkid.utils.Constant

class EntertainmentFragment : Fragment() {

    lateinit var binding: FragmentEntertainmentBinding
    private var listFilm: List<Video>? = null
    private var filmAdapter: VideoAdapter? = null
    private var sharePreferences: SharedPreferences? = null
    var allowClick = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).handleShowHeader(false)
        binding = FragmentEntertainmentBinding.inflate(layoutInflater)
        setDataFilm()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handle()
    }

    private fun setDataFilm() {

        listFilm = listOf(
            Video(R.drawable.baothuc, "Báo Thức Lo-Fi", R.raw.daydiongchauoi),
            Video(R.drawable.anhemga, "Phim Anh Em Nhà Gà", R.raw.anhem_nha_ga),
            Video(R.drawable.convit, "Một Con Vịt", R.raw.mot_con_vit),
            Video(R.drawable.joy_story, "Joy_Story", R.raw.joy_toys),
            Video(R.drawable.anhemga, "Phim Anh Em Nhà Gà", R.raw.anhem_nha_ga),
            Video(R.drawable.convit, "Một Con Vịt", R.raw.mot_con_vit),
            Video(R.drawable.joy_story, "Joy_Story", R.raw.joy_toys)
        )

        binding.rcFilm.apply {
            val layoutParams = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            layoutManager = layoutParams
            filmAdapter = VideoAdapter(listFilm ?: listOf())
            adapter = filmAdapter
        }
    }

    private fun handle() {
        sharePreferences =activity?.getSharedPreferences("POINT", Context.MODE_PRIVATE)  // ?: return
        val value = sharePreferences?.getString("pointtrue", " " )
        var point = value?.toInt()
        if (point != null) {
            if (point >= 3) {
                allowClick = true
                // gán lại giá trị về 0 , chỉ cho xem 1 video , muốn xem thêm thì học thêm
                point = 0
                val value = point.toString()
                sharePreferences = activity?.getSharedPreferences("POINT", Context.MODE_PRIVATE) //?: return
                with(sharePreferences?.edit()) {
                    this?.putString("pointtrue", value)
                    this?.apply()
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "Hãy hoàn thành 3 câu hỏi đúng ở phần luyện tập",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else
        {
            Toast.makeText(requireContext(), "null", Toast.LENGTH_SHORT).show()
        }

        //  `allowClick` là true thì cho click
        if (allowClick) {
            filmAdapter?.onClickItem = {
                val intent = Intent(requireActivity(), PlayVideo::class.java)
                intent.putExtra("key", it)
                startActivity(intent)

            }
        }

    binding.tvExit.setOnClickListener {
        findNavController().navigate(R.id.HomeFragment)
        }
    }


}