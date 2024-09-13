package com.tuan.englishforkid.presentation.profile


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentChangeBinding
import com.tuan.englishforkid.ext.showLoading
import com.tuan.englishforkid.model.UserResponse
import com.tuan.englishforkid.presentation.account.UserViewModel
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class ChangeProfileFragment : Fragment() {

    lateinit var binding: FragmentChangeBinding
    private var sharePreferences: SharedPreferences? = null
    private val viewModelChangeUsser: UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeBinding.inflate(layoutInflater)

        showProfile()
        binding.btnchange.setOnClickListener {
            changeProfile()
        }
        return binding.root
    }

    private fun changeProfile() {
        val newName = binding.tvName.text.toString()
        val newEmail = binding.tvEmail.text.toString()
        val newPass = binding.tvPass.text.toString()
        val userId = sharePreferences?.getString("id", "") ?: ""

        if (newName.isEmpty() || newEmail.isEmpty() || newPass.isEmpty()) {
            Toast.makeText(requireContext(), "Thông tin bị trống", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelChangeUsser.getUser().observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    try {
                        val value = data.data?.body() as? UserResponse
                        if (value?.listUser != null) {
                            val listUser = value.listUser
                            val currentUser = listUser?.find { it.iddata == userId.toInt() }

                            if (currentUser != null) {
                                var isChanged = false
                                var isConflict = false

                                // Kiểm tra email mới
                                if (newEmail != currentUser.gmail) {
                                    isChanged = true
                                    if (listUser.any { it.gmail == newEmail && it.iddata != userId.toInt() }) {
                                        Toast.makeText(requireContext(), "Email đã được sử dụng bởi tài khoản khác", Toast.LENGTH_SHORT).show()
                                        isConflict = true
                                    }
                                }

                                // Kiểm tra tên mới
                                if (newName != currentUser.nameuser) {
                                    isChanged = true
                                    if (listUser.any { it.nameuser == newName && it.iddata != userId.toInt() }) {
                                        Toast.makeText(requireContext(), "Tên người dùng đã được sử dụng bởi tài khoản khác", Toast.LENGTH_SHORT).show()
                                        isConflict = true
                                    }
                                }

                                // Kiểm tra mật khẩu mới
                                if (newPass != currentUser.pass) {
                                    isChanged = true
                                }

                                if (!isChanged) {
                                    Toast.makeText(requireContext(), "Không có thông tin nào được thay đổi", Toast.LENGTH_SHORT).show()
                                } else if (!isConflict) {
                                    updateProfile(userId.toInt(), newName, newEmail, newPass)
                                }
                            } else {
                                Toast.makeText(requireContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Có lỗi xảy ra khi kiểm tra thông tin", Toast.LENGTH_SHORT).show()
                    }
                }
                DataResult.Status.LOADING -> {
                    // Xử lý trạng thái loading nếu cần
                }
                DataResult.Status.ERROR -> {
                    Toast.makeText(requireContext(), "Lỗi khi lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateProfile(userId: Int, name: String, email: String, pass: String) {
        viewModelChangeUsser.putUserProfile(userId, name, email, pass).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                DataResult.Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.LoginFragment)
                }
                DataResult.Status.ERROR -> {
                    Toast.makeText(requireContext(), "Lỗi khi cập nhật thông tin: ${result.message}", Toast.LENGTH_SHORT).show()
                }
                DataResult.Status.LOADING -> {
                    // Hiển thị loading nếu cần
                }
            }
        }
    }


    private fun showProfile() {

        sharePreferences = activity?.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val userId = sharePreferences?.getString("id", null)  // Lấy id của người dùng từ SharedPreferences

        if (userId == null) {
            Toast.makeText(requireContext(), "Không tìm thấy id người dùng", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelChangeUsser.getUser().observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    try {
                        val value = data.data?.body() as? UserResponse
                        if (value?.listUser != null) {
                            val listUser = value.listUser

                            // Tìm người dùng có id trùng khớp
                            val user = listUser?.firstOrNull { it.iddata == userId.toInt() }

                            if (user != null) {
                            binding.tvName.text = Editable.Factory.getInstance().newEditable(user.nameuser)
                            binding.tvNameUser.text = Editable.Factory.getInstance().newEditable(user.nameuser)
                            binding.tvEmail.text = Editable.Factory.getInstance().newEditable(user.gmail)
                            binding.tvPass.text = Editable.Factory.getInstance().newEditable(user.pass)
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
