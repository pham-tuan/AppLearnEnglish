package com.tuan.englishforkid.presentation.practive

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
import com.tuan.englishforkid.databinding.FragmentSplashPracBinding

class SplashPracFragment : Fragment() {
    lateinit var binding: FragmentSplashPracBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowHeader(false)
        binding = FragmentSplashPracBinding.inflate(layoutInflater)
        setSplash()
        return binding.root
    }

    private fun setSplash() {
        Glide.with(this).load(R.drawable.spractice).into(binding.imglogo);

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.pactiveFragment2)
        },2000)
    }

}