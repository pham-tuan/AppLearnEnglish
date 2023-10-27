package com.tuan.englishforkid.presentation.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentRegisterBinding
import com.tuan.englishforkid.model.User
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding
    private val viewModelRegister: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this
        binding.registerViewModel = viewModelRegister
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            handleRegister()
        }

        binding.tvBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun handleRegister() {
        var agree = binding.chkAgree.isChecked
        val emailPattern = "@gmail\\.com$".toRegex()  //kiểm tra định dạng email

        if ((binding.edtname.text.toString().trim().isEmpty()) || (binding.edtemail.text.toString()
                .trim().isEmpty()) || (binding.edtpass.text.toString().trim().isEmpty())
        ) {
            Toast.makeText(context, "Bạn phải nhập đủ các thông tin", Toast.LENGTH_SHORT).show()

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edtemail.text).matches()) {

            Toast.makeText(context, "Chưa đúng định dạng email", Toast.LENGTH_SHORT).show()

        } else if (!binding.chkAgree.isChecked) {

            Toast.makeText(context, "Bạn chưa đồng ý với các điều khoản", Toast.LENGTH_SHORT).show()

        }  else {
                Toast.makeText(context, "thành công!!!", Toast.LENGTH_SHORT).show()
                putData()
                findNavController().navigate(R.id.LoginFragment)
            }

    }
    private fun putData(){
        val name = binding.edtname.text.toString()
        val email = binding.edtemail.text.toString()
        val pass = binding.edtpass.text.toString()

        viewModelRegister.postUser(name, email ,pass).observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "đăng kí thành công", Toast.LENGTH_SHORT).show()
                }

                DataResult.Status.LOADING -> {
                    Toast.makeText(requireContext(), "vào caseload", Toast.LENGTH_SHORT).show()
                }

                DataResult.Status.ERROR -> {
                    Toast.makeText(requireContext(), "Lỗi: " + data.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}