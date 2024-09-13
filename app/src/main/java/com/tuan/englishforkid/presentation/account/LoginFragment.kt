package com.tuan.englishforkid.presentation.account

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentLoginBinding
import com.tuan.englishforkid.model.User
import com.tuan.englishforkid.model.UserResponse
import com.tuan.englishforkid.presentation.home.HomeFragment
import com.tuan.englishforkid.utils.Constant
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private var sharePreferences: SharedPreferences? = null
    private val viewModelLogin: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowHeader(false)   // ẩn header menu
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this
        binding.loginViewModel = viewModelLogin
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharePreferences = activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        showDataFromSharePrefer()

        binding.btnLogin.setOnClickListener {
            HandleSingIn()
        }
        binding.tvdangky.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }
        binding.forgetPass.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_ForgetPassFragment)
        }

    }


    private fun HandleSingIn() {
        val UserName = binding.edtuser.text?.toString()?.trim()
        val PassWord = binding.edtpass.text?.toString()?.trim()

        if ((UserName.isNullOrEmpty() || PassWord.isNullOrEmpty())
        ) {
            Toast.makeText(context, "Bạn đang bỏ trống thông tin ", Toast.LENGTH_SHORT).show()
        } else {
            checkUsser(UserName,PassWord)
        }

    }

    private fun checkUsser(UserName: String?, PassWord: String?) {
        val IsCheck = binding.chkRememberUser.isChecked
        viewModelLogin.getUser().observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    try {
                        val listUser: ArrayList<User> = ArrayList()
                        val value = data.data?.body() as? UserResponse

                        if (value?.listUser != null) {
                            listUser.addAll(value.listUser!!)

                            if (listUser.isNotEmpty()) {
                                val matchedUser = listUser.find { it.nameuser == UserName && it.pass == PassWord }
                                if (matchedUser != null) {
                                    if (IsCheck) {
                                        saveData(UserName, PassWord)
                                       findNavController().navigate(R.id.action_LoginFragment_to_HomeFragment)
                                        sharePreferences?.edit()?.putBoolean(Constant.CHECK_LOGIN, true)?.apply()
                                    } else {
                                        sharePreferences?.edit()?.putBoolean(Constant.CHECK_LOGIN, true)?.apply()
                                        findNavController().navigate(R.id.action_LoginFragment_to_HomeFragment)
                                        clearSavedLoginData()
                                    }
                                    saveProfile(matchedUser)
                                } else {
                                    // Nếu tới đây, có nghĩa không có người dùng nào khớp
                                    Toast.makeText(
                                        requireContext(),
                                        "Bạn đã nhập sai tên hoặc mật khẩu",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.edtuser.text = null
                                    binding.edtpass.text = null
                                }
                            } else {
                                // Danh sách người dùng trống
                                Toast.makeText(
                                    requireContext(),
                                    "Danh sách người dùng trống",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Dữ liệu từ server bị null
                            Toast.makeText(
                                requireContext(),
                                "Dữ liệu từ server không hợp lệ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        // Log thông tin lỗi
                        Log.e("LoginFragment", "Error checking user", e)
                    }
                }
                DataResult.Status.LOADING -> {
                    // Loading state
                }
                DataResult.Status.ERROR -> {
                    Toast.makeText(requireContext(), "lỗi data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearSavedLoginData() {
        sharePreferences = activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        sharePreferences?.edit()?.apply {
            remove("NameLogin")
            remove("PassWord")
            putBoolean("SavePass", false)
            apply()
        }
    }

    private fun saveProfile(user: User) {
        sharePreferences = activity?.getSharedPreferences("USER", Context.MODE_PRIVATE)
        sharePreferences?.edit()?.apply {
            putString("Namep", user.nameuser)
            putString("Gmailp", user.gmail)
            putString("Passp", user.pass)
            putString("id", user.iddata.toString())
            apply()
        }

    }

    private fun saveData(username: String?, password: String?) {
        sharePreferences = activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        sharePreferences?.edit()?.apply {
            putString("NameLogin", username)
            putString("PassWord", password)
            putBoolean("SavePass", binding.chkRememberUser.isChecked)
            apply()
        }
    }

    private fun showDataFromSharePrefer() {
        sharePreferences = activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        sharePreferences?.let {
            val data1 = it.getString("NameLogin", "")
            val data2 = it.getString("PassWord", "")
            val data3 = it.getBoolean("SavePass", false)
            binding.edtuser.setText(data1)
            binding.edtpass.setText(data2)
            binding.chkRememberUser.isChecked = data3
        } ?: showToast("null")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}