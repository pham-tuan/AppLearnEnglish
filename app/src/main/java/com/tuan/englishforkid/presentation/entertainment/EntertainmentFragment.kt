package com.tuan.englishforkid.presentation.entertainment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentEntertainmentBinding
import com.tuan.englishforkid.model.Topic
import com.tuan.englishforkid.utils.Constant

class EntertainmentFragment : Fragment() {

    private lateinit var binding: FragmentEntertainmentBinding
    private var listFilm: List<Video> = listOf()
    private var filmAdapter: VideoAdapter? = null
    private var sharedPreferences: SharedPreferences? = null
    private var dialog: AlertDialog? = null
    private var allowClick = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

            Video(R.drawable.anhemga, "Phim Anh Em Nhà Gà", R.raw.anhem_nha_ga),
            Video(R.drawable.piper, "Piper", R.raw.piper),
            Video(R.drawable.burrow, "burrow", R.raw.burrow),
            Video(R.drawable.convit, "Một Con Vịt", R.raw.mot_con_vit),
            Video(R.drawable.joy_story, "Joy_Story", R.raw.joy_toys),
            Video(R.drawable.party, "Party-Cloudy", R.raw.partlycloudy),
            Video(R.drawable.laluna, "La-Luna", R.raw.laluna),
            Video(R.drawable.hope, "Hope", R.raw.hope)
        )

        binding.rcFilm.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            filmAdapter = VideoAdapter(listFilm)
            adapter = filmAdapter
        }
    }

    private fun handle() {
        sharedPreferences = activity?.getSharedPreferences("POINT", Context.MODE_PRIVATE)
        val value = sharedPreferences?.getString("pointtrue", "0")
        var point = value?.toIntOrNull() ?: 0

        if (point >= 3) {
            allowClick = true
            point = 0
            sharedPreferences?.edit()?.putString("pointtrue", point.toString())?.apply()
        } else {
            showdialog()
        }

        if (allowClick) {
            filmAdapter?.onClickItem = {
                val intent = Intent(requireActivity(), PlayVideo::class.java).apply {
                    putExtra("key", it)
                }
                startActivity(intent)
            }
        }

        binding.tvExit.setOnClickListener {
            findNavController().navigate(R.id.HomeFragment)
        }
    }

    private fun showdialog() {
        val alertDialogBuilder = AlertDialog.Builder(activity, R.style.Themecustom)
        val view = layoutInflater.inflate(R.layout.customdialog, null)
        alertDialogBuilder.setView(view).setCancelable(false)

        view.findViewById<TextView>(R.id.go).setOnClickListener {
            dialog?.dismiss()
            findNavController().navigate(R.id.pactiveFragment)
        }
        view.findViewById<TextView>(R.id.skip).setOnClickListener {
            dialog?.dismiss()
            findNavController().navigate(R.id.HomeFragment)
        }

        dialog = alertDialogBuilder.create()
        dialog?.show()
    }
}