package com.tuan.englishforkid.presentation.account

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentForgetPassBinding
import com.tuan.englishforkid.databinding.FragmentHomeBinding
import com.tuan.englishforkid.model.UserResponse
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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
class ForgetPassFragment : Fragment() {
    private var _binding: FragmentForgetPassBinding? = null
    private val binding get() = _binding!!
    private val viewModelForget: UserViewModel by viewModels()
    private val code = Random.nextInt(8999) + 1000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentForgetPassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        binding.back.setOnClickListener { findNavController().navigateUp() }
        binding.tvReissue.setOnClickListener { handleReissueClick() }
    }

    private fun handleReissueClick() {
        val email = binding.mail.text.toString()
        when {
            email.isEmpty() -> showToast("Bạn phải nhập đủ các thông tin")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                showToast("Chưa đúng định dạng email")
            else -> checkEmail(email)
        }
    }

    private fun checkEmail(email: String) {
        viewModelForget.getUser().observe(viewLifecycleOwner) { result ->
            when (result.status) {
                DataResult.Status.SUCCESS -> handleUserData(result.data?.body(), email)
                DataResult.Status.ERROR -> showToast("Lỗi khi lấy dữ liệu người dùng: ${result.message}")
                DataResult.Status.LOADING -> { /* Handle loading state if needed */ }
            }
        }
    }

    private fun handleUserData(userResponse: UserResponse?, email: String) {
        val userExists = userResponse?.listUser?.any { it.gmail == email } ?: false
        if (userExists) {
            viewLifecycleOwner.lifecycleScope.launch {
                val sendResult = sendNewPass(email)
                if (sendResult) {
                    showToast("Đã gửi mật khẩu thành công")
                    updateNewPassword(email)
                    Log.d("newpass", "Code: $code")
                } else {
                    showToast("Gửi mật khẩu thất bại")
                }
            }
        } else {
            showToast("Email này chưa được đăng ký")
        }
    }

    private suspend fun sendNewPass(toEmail: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val props = Properties().apply {
                    put("mail.smtp.host", "74.125.133.109")
                    put("mail.smtp.port", "587")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                }

                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication() =
                        PasswordAuthentication("geezmussic@gmail.com", "alxvggqassonsket")
                })

                MimeMessage(session).apply {
                    setFrom(InternetAddress("geezmussic@gmail.com"))
                    setRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
                    subject = "Android App EnglishForKids"
                    setText("Mật khẩu mới: $code")
                    Transport.send(this)
                }
                true
            } catch (e: Exception) {
                Log.e("ForgetPassFragment", "Error sending email", e)
                false
            }
        }
    }

    private fun updateNewPassword(email: String) {
        Log.d("UpdatePassword", "Bắt đầu cập nhật mật khẩu cho email: $email")
        viewModelForget.putNewPass(email, code.toString()).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                DataResult.Status.SUCCESS -> {
                    showToast("Cập nhật mật khẩu thành công")
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (isAdded) {
                            findNavController().navigateUp()
                        }
                    }, 1000)
                }
                DataResult.Status.ERROR -> {
                    Log.e("UpdatePassword", "Lỗi: ${result.message}")
                    showToast("Cập nhật mật khẩu thất bại: ${result.message}")
                }
                DataResult.Status.LOADING -> {

                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}