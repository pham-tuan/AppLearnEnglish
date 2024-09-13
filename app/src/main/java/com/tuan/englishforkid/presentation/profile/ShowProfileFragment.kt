package com.tuan.englishforkid.presentation.profile

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentShowProfileBinding
import com.tuan.englishforkid.model.User
import com.tuan.englishforkid.model.UserResponse
import com.tuan.englishforkid.presentation.account.UserViewModel
import com.tuan.englishforkid.utils.Constant
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowProfileFragment : Fragment() {

    lateinit var binding: FragmentShowProfileBinding
    private var sharePreferences: SharedPreferences? = null
    private val viewModelUser: UserViewModel by viewModels()
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

        sharePreferences = activity?.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val userId = sharePreferences?.getString("id", null)  // Lấy id của người dùng từ SharedPreferences

        if (userId == null) {
            Toast.makeText(requireContext(), "Không tìm thấy id người dùng", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelUser.getUser().observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    try {
                        val value = data.data?.body() as? UserResponse
                        if (value?.listUser != null) {
                            val listUser = value.listUser

                            // Tìm người dùng có id trùng khớp
                            val user = listUser?.firstOrNull { it.iddata == userId.toInt() }

                            if (user != null) {
                                binding.tvName.text = user.nameuser
                                binding.tvEmail.text = user.gmail
                                binding.tvPass.text = user.pass
                                binding.tvNameUser.text = user.nameuser
                            } else {
                                Toast.makeText(requireContext(), "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), "Dữ liệu từ server không hợp lệ", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                    }
                }
                DataResult.Status.LOADING -> {
                    // Loading state
                }
                DataResult.Status.ERROR -> {
                    Toast.makeText(requireContext(), "Lỗi data", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}