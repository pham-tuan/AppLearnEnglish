package com.tuan.englishforkid.presentation.splash

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tuan.englishforkid.R
import com.tuan.englishforkid.utils.Constant


class SplashFragment : Fragment() {
    private var sharePreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharePreferences = activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)

        val isLoggedIn = sharePreferences?.getBoolean(Constant.CHECK_LOGIN, false)
        Log.d("hung", "onViewCreated: $isLoggedIn")
        callView(isLoggedIn)
    }

    private fun callView(isLoggedIn: Boolean?) {
        if(isLoggedIn == false){
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            },500)
        }
        else{
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_HomeFragment)
            },500)
        }

    }

}