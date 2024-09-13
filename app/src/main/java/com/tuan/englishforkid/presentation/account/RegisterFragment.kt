package com.tuan.englishforkid.presentation.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentRegisterBinding
import com.tuan.englishforkid.ext.showLoading
import com.tuan.englishforkid.model.User
import com.tuan.englishforkid.model.UserResponse
import com.tuan.englishforkid.utils.Constant
import com.tuan.englishforkid.utils.DataResult
import com.tuan.englishforkid.utils.DataResult.Status.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModelRegister: UserViewModel by viewModels()
    private val code = Random.nextInt(8999) + 1000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.registerViewModel = viewModelRegister
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener { handleRegister() }
        binding.tvBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun handleRegister() {
        val name = binding.edtname.text.toString()
        val email = binding.edtemail.text.toString()
        val pass = binding.edtpass.text.toString()
        val agree = binding.chkAgree.isChecked

        when {
            name.isEmpty() || email.isEmpty() || pass.isEmpty() ->
                showToast("Bạn phải nhập đủ các thông tin")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                showToast("Chưa đúng định dạng email")
            !agree ->
                showToast("Bạn chưa đồng ý với các điều khoản của chúng tôi")
            else -> checkUser(name, email ,pass)
        }
    }

    private fun checkUser(name: String, email: String, pass: String) {
        viewModelRegister.getUser().observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    try {
                        val value = data.data?.body() as? UserResponse
                        if (value?.listUser != null) {
                            val listUser = value.listUser
                            val existingUserByEmail = listUser?.firstOrNull { it.gmail == email }
                            val existingUserByName = listUser?.firstOrNull { it.nameuser == name }

                            if (existingUserByEmail != null) {
                                showToast("Email đã được đăng ký")
                                binding.edtemail.setText("")
                            } else if (existingUserByName != null) {
                                showToast("Tên người dùng đã tồn tại")
                                binding.edtname.setText("")
                            } else {
                                sendCode(name, email, pass)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("RegisterFragment", "Có lỗi xảy ra", e)
                    }
                }
                DataResult.Status.LOADING -> {
                    // Loading state
                }
                DataResult.Status.ERROR -> {
                    showToast("Lỗi khi lấy dữ liệu người dùng")
                }
            }
        }
    }

    private fun sendCode(name: String, email: String, pass: String) {
        val bundle = Bundle().apply {
            putString("name", name)
            putString("email", email)
            putString("password", pass)
            putString(Constant.CODE, code.toString())
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
            try {
                val result = sendOTPAsync()
                if (result) {
                    Toast.makeText(requireContext(), "Mã xác nhận đã được gửi", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_Register_to_Confirmcode, bundle)
                } else {
                    Toast.makeText(requireContext(), "Gửi mã xác nhận thất bại", Toast.LENGTH_SHORT).show()
                    Log.e("SendCode", "lỗi gửi code ")
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Lỗi khi gửi mã xác nhận: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("SendCode", "Exception khi gửi code ", e)
            }
        }

    }

    private suspend fun sendOTPAsync(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val senderEmail = "geezmussic@gmail.com"
                val receiverEmail = binding.edtemail.text?.toString()
                val passwordSenderEmail = "alxvggqassonsket"

                val props = Properties().apply {
                    put("mail.smtp.host", "74.125.133.109")
                    put("mail.smtp.port", "587")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                }

                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(senderEmail, passwordSenderEmail)
                    }
                })

                MimeMessage(session).apply {
                    setFrom(InternetAddress("geezmussic@gmail.com"))
                    setRecipient(Message.RecipientType.TO, InternetAddress(receiverEmail))
                    subject = "Android App EnglishForKids"
                    setText("Mã Đăng Kí OTP, \n\nVerify Code: $code")
                    Transport.send(this)
                }
                true
            } catch (e: Exception) {
            Log.e("ForgetPassFragment", "Error sending email", e)
            false
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
