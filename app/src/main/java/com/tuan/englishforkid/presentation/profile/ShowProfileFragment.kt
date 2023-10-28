package com.tuan.englishforkid.presentation.profile

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentShowProfileBinding

class ShowProfileFragment : Fragment() {

    lateinit var binding: FragmentShowProfileBinding
    private var sharePreferences : SharedPreferences?= null
    private var sharePreferences2 : SharedPreferences?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowProfileBinding.inflate(layoutInflater)
        showprofile()
        return binding.root
    }

    private fun showprofile() {

        sharePreferences = activity?.getSharedPreferences("USER", Context.MODE_PRIVATE) //?: return
        val name = sharePreferences?.getString("Namep", "")
        val email = sharePreferences?.getString("Gmailp", "")
        val pass = sharePreferences?.getString("Passp", "")
        binding.tvName.setText(name)
        binding.tvEmail.setText(email)
        binding.tvPass.setText(pass)
        binding.tvNameUser.setText(name)

//        sharePreferences2 = activity?.getSharedPreferences("SELECTIMAGE", Context.MODE_PRIVATE)
//        val selectedImageUriString = sharePreferences2?.getString("selected_image_uri", "")
//        val selectedImageUri = Uri.parse(selectedImageUriString)
//        if (selectedImageUriString.isNullOrEmpty()) {
//            binding.profileimage.setImageResource(R.drawable.avata)
//        } else {
//            val selectedImageUri = Uri.parse(selectedImageUriString)
//            binding.profileimage.setImageURI(selectedImageUri)
//        }


    }


}