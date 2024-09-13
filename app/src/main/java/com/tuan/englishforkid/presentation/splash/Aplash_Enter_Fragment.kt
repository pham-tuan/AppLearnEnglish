package com.tuan.englishforkid.presentation.entertainment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentAplashEnterBinding

class Aplash_Enter_Fragment : Fragment() {
    lateinit var binding: FragmentAplashEnterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowHeader(false)
        binding = FragmentAplashEnterBinding.inflate(layoutInflater)
        setSplash()
        return binding.root
    }

    private fun setSplash() {
        Glide.with(this).load(R.drawable.senter).into(binding.imglogo);

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.EntertainmentFragment)
        },2000)
    }

}